# How Bouncing Balls Is Built

Run `edu.uw.tcss.bouncingballs.app.BouncingBallsApp`.

## Things to try

- **Launch a ball.** Press on the board, drag, and let go. The ball flies the opposite way from your drag, like a slingshot, and a longer drag launches it faster. The left button makes a purple ball and the right button makes a gold one.
- **Shockwave.** Shift+click anywhere. Every ball is pushed away from the click, and balls closer to it get a bigger push.
- **Edit a ball.** Press Pause, then click a ball to change its speed, direction and color. Press Resume to continue.
- **Gravity.** Turn gravity on and the balls fall, bounce lower each time, and come to rest on the floor.
- **The left wall** gives a ball a speed boost, with a gold flash and a short glowing trail.
- **The bar at the bottom** shows the total kinetic energy of all the balls. Watch it drop as friction slows them and jump after a shockwave.

## Four packages

| Package | What it holds |
|---|---|
| `model` | The data. `Ball` stores one ball's position, velocity, radius, mass and color. `SimulationState` holds the list of balls and the paused and gravity flags. |
| `physics` | All the math. `PhysicsEngine` moves the balls, bounces them off the walls and each other, and applies gravity, friction and shockwaves. |
| `view` | Everything Swing. `SimulationFrame` builds the window and owns the timer. `SimulationPanel` draws the balls and handles the mouse. `ControlPanel`, `BallEditorOverlay` and `StatusBar` are the buttons, the ball editor and the energy bar. |
| `app` | `BouncingBallsApp`, which starts the program on the Event Dispatch Thread. |

The `physics` package imports nothing from Swing. That is what lets JUnit test every formula without opening a window. The tests are in `test/edu/uw/tcss/bouncingballs`.

## One tick

A `javax.swing.Timer` in `SimulationFrame` fires every 10 milliseconds. Each tick, unless the simulation is paused:

1. `PhysicsEngine.update(state)` advances every ball one step, in this order:
    - apply forces (gravity, drag and friction)
    - move each ball by its velocity
    - bounce balls off the walls
    - resolve ball-to-ball collisions
2. `update` returns the balls that hit the left wall, and `SimulationPanel` starts their glow effect.
3. `StatusBar` shows the new total energy.
4. `SimulationPanel` calls `repaint()`, and its `paintComponent` draws the balls where they are now.

This is the same loop as the lecture's `MovingShapePanel`, where a timer ticks, the state takes one step, and the panel repaints. The difference is that the state and the math live in their own classes instead of inside the panel.

## The physics

Positions are the **center** of each ball, which keeps the collision math simple. `paintComponent` subtracts the radius to find the corner of the bounding box it draws.

**Mass.** A ball's mass is `DENSITY * PI * radius * radius`, so it grows with the ball's area and a bigger ball is heavier.

**Do two balls touch?** `isColliding` compares the distance between their centers with the sum of their radii. It compares the squares, `dx*dx + dy*dy < (r1 + r2) * (r1 + r2)`, so it never needs a square root.

**Bouncing two balls apart.** `resolveCollision` works along the line between the two centers, called the collision normal.

1. If the balls overlap, each one is pushed back along the normal by half the overlap, so they can't get stuck together.
2. If they are moving toward each other, each ball's velocity changes along the normal by an amount based on the masses: `2 * m2 / (m1 + m2)` for the first ball and `2 * m1 / (m1 + m2)` for the second. That is the formula for an elastic collision in physics, so total momentum is unchanged and a heavy ball barely slows when a light one hits it.

**Friction, with gravity off.** Each tick, a ball's velocity is multiplied by `0.995` raised to the power `REFERENCE_MASS / mass`. A heavier ball has a smaller exponent, so it keeps more of its speed and takes longer to stop.

**Gravity.** With gravity on, each tick adds `0.15` to the vertical velocity and multiplies both velocity components by `0.9985` for air drag. A floor bounce keeps 85% of the vertical speed. Balls on the floor also feel friction, and a ball on the floor moving slower than `0.1` stops completely.

**The left wall.** Hitting it multiplies a ball's velocity by `1.25`, unless the ball is already moving at speed `12` or faster.

**Shockwave.** Each ball gets a push away from the click of `500 / distance`, so a ball twice as far away gets half the push. The distance is never treated as less than 1, so a click right on a ball can't produce an infinite push.

**Energy.** The total is the sum of `(1/2) * mass * speed * speed` over all the balls.

## How it was made

The program was built by directing a coding agent. The prompts are in `requirements.md`.
