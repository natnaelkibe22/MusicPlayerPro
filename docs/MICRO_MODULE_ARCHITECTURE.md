# Micro-Module Architecture

Build each feature like a small contained service. The public surface is a repository/controller class, the implementation is isolated, and the UI calls only those contracts.

This makes Codex/agent work safer: implement one module, run one Maestro test, move to the next module.
