# Contributing to Controller+

Thanks for helping improve Controller+.

## Development requirements

- Use a 64-bit Java 25 JDK.
- Target Minecraft 26.1.2.
- Use the included Gradle wrapper.
- Use an IDE version that understands Java 25.

## Change guidelines

- Keep each change focused.
- Follow the existing code style and package structure.
- Explain API and behaviour changes in the pull request.
- Include automated tests or clear manual test notes where appropriate.
- Test common gameplay logic on a dedicated server.
- Do not submit copied AE2 code or assets.
- Do not introduce dependencies on AE2 internals without prior discussion and
  explicit compatibility documentation.

Before opening a pull request, run:

```powershell
.\gradlew.bat clean build
```

Use clear commit messages, for example:

```text
feat: add controller energy buffer
fix: prevent duplicate grid energy injection
docs: document AE2 integration limitations
refactor: isolate controller energy logic
build: configure Java 25 toolchain
test: add buffer clamping tests
```

Update `CHANGELOG.md` describing your change from a player's perspective.

## License

By contributing, you agree that your contributions will be licensed under the
same terms as the project: [GPL-3.0](LICENSE) for code, and
[CC BY-NC-SA 3.0](licenses/LICENSE-ASSETS) for original textures, models, and
other visual assets (see [NOTICE](licenses/NOTICE)).

