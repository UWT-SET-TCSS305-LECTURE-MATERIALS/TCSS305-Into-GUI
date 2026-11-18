# The Prompts Behind Bouncing Balls

Bouncing Balls started as a single class. Its balls all had one size and one speed, moved only diagonally, and bounced off each other even when they weren't quite touching. A coding agent turned it into the program in this package, directed by the 14 prompts below, given in order. They are copied exactly as they were written, typos included.

Three things to look for:

- Most prompts describe what the user should see and feel, and leave the code to the agent. Prompt 6 asks for friction that is "noticeable but not too fast", which only a person running the program can judge.
- Prompts 7 and 12 ask the agent to propose ideas before building anything. That keeps the person in charge of the decisions about how the program looks.
- Prompt 14 asks for separate units and tests without naming a design. Compare it with `ARCHITECTURE.md` to see what the agent made of it.

---

1. Fix the collision - balls shouldn't bounce when they're not actually touching, only when the circles overlap

2. Sometimes balls get stuck together instead of bouncing apart - fix this

3. Make movement more realistic - balls should move in any direction, not just diagonal. Random starting direction for each ball.

4. Balls should move at different speeds. Random speed when created.

5. Make balls different sizes. Bigger balls should be "heavier" and this should affect how they bounce off each other.

6. Add a slight amount of friction to the surface the balls move around on so that they slowly stop moving over time. This amount of slowdown should be noticeable but not too fast to slow down. The mass of the ball should affect the amount of slowing based on correct physics models.

7. Add an accelerated "bounce" to the left wall when the ball hits it. I.e the ball gets more speed added to its direction when it hits the left wall. Make some visual indication that this happened. Present some ideas to me the user for what this should look like.

8. Add a way to toggle gravity on and off. When gravity is on, the balls should fall toward the bottom and bounce off the floor. The surface friction goes away but the balls should bounce less and less each time they hit the ground, like a real ball bouncing until it stops. When gravity is off, they just float around like they do now.

9. Add pause, resume, and reset buttons

10. When paused, clicking a ball should let you edit its properties (speed, direction, etc.) in a side panel but I don't want to see the panel when the balls are moving only when its paused. Make the side panel show up on top of the current board, don't make the window bigger.

11. When spawning a new ball, I want to be able to click and drag to give it an initial speed and direction - like a slingshot. The farther I drag, the faster it goes. Show me some kind of visual while I'm dragging so I can see where it's going to go and how fast.

12. Add some mechanism for the user to click on the surface and this will cause a shockwave that will speed up all of the balls, the speed of which will be proportional to the distance from the center of the click. Closer to the click, faster, farther from the click, slower. Present some ideas to me the user for what this should look like and what the mechanism could be.

13. Show me the total energy in the system somewhere on the screen - like a running number or a bar. Put it at the bottom of the window like that strip I see at the bottom of other programs. I want to see it go down when friction slows things and spike when I do a shockwave.

14. Write this as if you're my Computer Science colleague at the University who teaches software and programming classes. Use good programming practices that I'm not familiar with. They told me to keep most of the code separate into different units. Separate the drawing stuff from the physics calculations - I shouldn't see math mixed in with the visual stuff. I also want to be able to verify that the physics and math calculations are correct. They told me to test. ¯\_(ツ)_/¯
