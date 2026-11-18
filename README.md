# TCSS305-Into-GUI

TCSS 305 Introduction to GUI and Event-Driven Programming

TCSS 305 - Charles Bryan

Autumn 2026

Example code for the GUI concepts and Swing components covered in lecture.

Packages, all under `edu.uw.tcss.view`:

- `first.actionlisteners`: first event handlers with `ActionListener`
- `layouts`: Swing layout managers
- `mouselisteners`: mouse listeners
- `keylisteners`: key listeners
- `menus`: menus and menu items
- `graphics`: 2D graphics
- `graphics.animation`: 2D graphics animation with a Swing `Timer`

`edu.uw.tcss.logging` holds the `LoggerManager` helper.

`edu.uw.tcss.bouncingballs` is a larger animation: balls of different sizes and masses that collide realistically, with gravity, friction, a slingshot launcher and a shockwave. It is split into `model`, `physics` and `view` packages, and the physics has JUnit tests in `test/`. Run `app.BouncingBallsApp`, and read the package's `ARCHITECTURE.md` for how it works and `requirements.md` for the prompts a coding agent built it from.

`edu.uw.tcss.app` holds `PayrollBatch` and `PayrollTerminal`, the same payroll job run as a batch program and as an interactive terminal program, for contrast with event-driven GUIs.

Most classes are standalone and include their own `main` method, so you can run each one by itself.
