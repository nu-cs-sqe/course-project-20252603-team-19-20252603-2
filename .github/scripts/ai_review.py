#!/usr/bin/env python3
"""AI-powered PR reviewer using DeepSeek's API.

Posts an advisory sticky comment on the PR. Always exits 0 — never blocks merge.
Designed to be invoked from .github/workflows/ai-review.yml.

Required environment:
  DEEPSEEK_API_KEY    - DeepSeek API key (from repo secrets)
  GH_TOKEN            - GH-Actions-provided token (used by gh CLI)
  GITHUB_REPOSITORY   - "<owner>/<repo>"
  PR_NUMBER           - the PR number
  BASE_SHA            - PR's merge base
  HEAD_SHA            - PR's head commit
"""

import json
import os
import subprocess
import sys
import urllib.error
import urllib.request
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[2]
STICKY_MARKER = "<!-- ai-reviewer:sticky -->"
DEEPSEEK_URL = "https://api.deepseek.com/chat/completions"
MODEL = "deepseek-v4-pro"
MAX_PROMPT_CHARS = 100_000
TEMPERATURE = 0.2

SYSTEM_PROMPT = """\
You are an AI code reviewer for a Northwestern CS 380 (Software Quality
Engineering) course project — a Java implementation of Exploding Kittens, with
the team targeting the A-tier rubric. Review one pull request against the
standards in the STANDARDS section of the user message and output an advisory
sticky comment in the EXACT structure below. You do NOT block merges.

Severity icons:
  🔴 likely violation of a standard (concrete miss; address before merge)
  🟡 suggestion / judgment call
  ✅ affirmation (a standard is met)
  🟠 AI infrastructure issue (PR too large, malformed, etc.)

Output EXACTLY this markdown — no preamble, no postamble, no surrounding
code fences:

<!-- ai-reviewer:sticky -->
## 🤖 AI Review (DeepSeek v4 Pro)

### Use-cases doc as spec
- <icon> <finding with file paths / SHAs where helpful>

### BVA discipline
- ...

### TDD ordering
- ...

### Sole authorship
- ...

### Substantive commits
- ...

### Clean Code
- ...

### i18n
- ...

### Testing conventions
- ...

### Summary
<one-line tally, e.g. "0 🔴, 2 🟡, 6 ✅">

Rules:
- If a section has nothing to flag, give it a single ✅ one-liner.
- Be specific: name file paths, method names, commit SHAs.
- Do NOT invent violations. Use 🟡 "Could not verify because ..." over 🔴 when
  uncertain.
- Do NOT include any text outside the structure above.
"""


def run(cmd):
    return subprocess.run(
        cmd, capture_output=True, text=True, check=True
    ).stdout


def read_text(rel_path):
    p = REPO_ROOT / rel_path
    return p.read_text(encoding="utf-8") if p.exists() else ""


def gather_inputs(base_sha, head_sha):
    bva_dir = REPO_ROOT / "docs" / "bva"
    bva_files = sorted(p.name for p in bva_dir.glob("bva-*.md"))
    bva_contents = {n: read_text(f"docs/bva/{n}") for n in bva_files}

    changed = run(["git", "diff", "--name-only", base_sha, head_sha]).splitlines()
    full_files = {
        f: read_text(f)
        for f in changed
        if f.endswith(".java") and (REPO_ROOT / f).exists()
    }

    return {
        "standards": read_text("docs/STANDARDS.md"),
        "design_doc": read_text("docs/design/design-doc.md"),
        "use_cases": read_text("docs/use-cases/use-cases.md"),
        "bva_template": (
            read_text("docs/bva/bva-template.md")
            or read_text("docs/bva/README.md")
        ),
        "bva_files": bva_files,
        "bva_contents": bva_contents,
        "commits": run([
            "git", "log", "--pretty=fuller", "--stat",
            f"{base_sha}..{head_sha}",
        ]),
        "diff": run(["git", "diff", base_sha, head_sha]),
        "full_files": full_files,
    }


def render_full_files(files):
    block = "\n# FULL CONTENT OF CHANGED FILES\n"
    for path, content in files.items():
        block += f"\n## {path}\n\n```java\n{content}\n```\n"
    return block


def render_bva(bva):
    block = "\n# BVA FILE CONTENTS\n"
    for name, content in bva.items():
        block += f"\n## {name}\n\n```markdown\n{content}\n```\n"
    return block


def build_prompt(inputs):
    bva_inventory = "\n".join(f"- {n}" for n in inputs["bva_files"]) or "(empty)"
    base = (
        "# STANDARDS\n\n" + inputs["standards"]
        + "\n\n# DESIGN DOC (binding spec for class data members + methods)\n\n"
        + inputs["design_doc"]
        + "\n\n# USE-CASES (player-facing flows; companion to the design doc, "
        "NOT the structural spec)\n\n" + inputs["use_cases"]
        + "\n\n# BVA TEMPLATE / CONVENTION\n\n" + inputs["bva_template"]
        + "\n\n# BVA INVENTORY (files currently in docs/bva/)\n\n"
        + bva_inventory
        + "\n\n# PR COMMIT HISTORY (oldest first)\n\n```\n"
        + inputs["commits"] + "\n```\n"
        + "\n# PR DIFF\n\n```diff\n" + inputs["diff"] + "\n```\n"
    )

    full_files = dict(inputs["full_files"])
    bva = dict(inputs["bva_contents"])
    trimmed = []

    prompt = base + render_full_files(full_files) + render_bva(bva)

    while len(prompt) > MAX_PROMPT_CHARS and full_files:
        worst = sorted(full_files.keys())[-1]
        del full_files[worst]
        trimmed.append(worst)
        prompt = base + render_full_files(full_files) + render_bva(bva)

    while len(prompt) > MAX_PROMPT_CHARS and bva:
        worst = sorted(bva.keys())[-1]
        del bva[worst]
        trimmed.append(f"docs/bva/{worst}")
        prompt = base + render_full_files(full_files) + render_bva(bva)

    if trimmed:
        prompt += (
            "\n\n# TRIMMED\n\nThe following files were trimmed to fit context: "
            + ", ".join(trimmed)
        )

    return prompt


def call_deepseek(api_key, prompt):
    body = json.dumps({
        "model": MODEL,
        "messages": [
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": prompt},
        ],
        "temperature": TEMPERATURE,
        "stream": False,
    }).encode("utf-8")
    req = urllib.request.Request(
        DEEPSEEK_URL, data=body,
        headers={
            "Authorization": f"Bearer {api_key}",
            "Content-Type": "application/json",
        },
    )
    with urllib.request.urlopen(req, timeout=300) as resp:
        data = json.loads(resp.read().decode("utf-8"))
    choice = data["choices"][0]
    print(
        f"DeepSeek finish_reason: {choice.get('finish_reason', 'unknown')}",
        file=sys.stderr,
    )
    msg = choice["message"]
    return msg.get("content") or msg.get("reasoning_content") or ""


def find_sticky_comment(repo, pr):
    raw = run([
        "gh", "api", f"repos/{repo}/issues/{pr}/comments", "--paginate",
    ])
    comments = json.loads(raw) if raw.strip() else []
    for c in comments:
        if STICKY_MARKER in c.get("body", ""):
            return c["id"]
    return None


def post_or_update(repo, pr, body):
    path = "/tmp/ai_review_body.md"
    Path(path).write_text(body, encoding="utf-8")
    existing = find_sticky_comment(repo, pr)
    if existing is None:
        run(["gh", "pr", "comment", str(pr), "--body-file", path])
    else:
        run([
            "gh", "api",
            f"repos/{repo}/issues/comments/{existing}",
            "-X", "PATCH", "-F", f"body=@{path}",
        ])


def fallback(reason):
    return (
        f"{STICKY_MARKER}\n"
        "## 🤖 AI Review (DeepSeek v4 Pro)\n\n"
        f"🟠 AI review unavailable for this run: {reason}.\n\n"
        "Self-check this PR against `docs/STANDARDS.md`."
    )


def main():
    api_key = os.environ.get("DEEPSEEK_API_KEY")
    repo = os.environ.get("GITHUB_REPOSITORY")
    pr = os.environ.get("PR_NUMBER")
    base_sha = os.environ.get("BASE_SHA")
    head_sha = os.environ.get("HEAD_SHA")

    missing = [k for k, v in {
        "DEEPSEEK_API_KEY": api_key,
        "GITHUB_REPOSITORY": repo,
        "PR_NUMBER": pr,
        "BASE_SHA": base_sha,
        "HEAD_SHA": head_sha,
    }.items() if not v]
    if missing:
        print(f"missing env vars: {missing}", file=sys.stderr)
        return

    commit_count = run([
        "git", "rev-list", "--count", f"{base_sha}..{head_sha}"
    ]).strip()
    if commit_count == "0":
        print("no commits in PR; skipping review")
        return

    try:
        inputs = gather_inputs(base_sha, head_sha)
        prompt = build_prompt(inputs)
        review = call_deepseek(api_key, prompt)
    except (urllib.error.URLError, urllib.error.HTTPError) as e:
        print(f"deepseek API error: {e}", file=sys.stderr)
        post_or_update(repo, pr, fallback(f"API error ({e})"))
        return
    except subprocess.CalledProcessError as e:
        print(f"shell command failed: {e.cmd}\n{e.stderr}", file=sys.stderr)
        post_or_update(repo, pr, fallback("input gathering failed"))
        return
    except Exception as e:
        print(f"unexpected error: {type(e).__name__}: {e}", file=sys.stderr)
        post_or_update(repo, pr, fallback("unexpected error"))
        return

    if STICKY_MARKER not in review:
        review = (
            f"{STICKY_MARKER}\n"
            "## 🤖 AI Review (DeepSeek v4 Pro)\n\n"
            "🟠 Model output did not match expected structure. Raw output:\n\n"
            f"{review}"
        )

    post_or_update(repo, pr, review)
    print("review posted.")


if __name__ == "__main__":
    main()
