import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class FinalProject extends Cow {

/*
 Toolkit toolkit = Toolkit.getDefaultToolkit();
 Image emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); 
 Cursor invisibleCursor = toolkit.createCustomCursor(emptyImage, new Point(0, 0), "Invisible Cursor");
 _jFrame.getContentPane().setCursor(invisibleCursor);
 
 the above chunk of code was added to _cow_safe_attempt_initialize() so that the frame is initialized with a hidden cursor
 the crosshair functions as the cursor to prevent confusion when shooting
 */
	
	static class Target {
		boolean inUse;
		double x; // positionX
		double y; // positionY
		double radius;
		Color color;
		
		static final int targetSize_micro = 5;
		static final int targetSize_normal = 10;

		void draw() {
			// TODO
			drawCircle(this.x, this.y, this.radius, this.color);
		}
		
		// make random coords for targets in flicking gamemode
		static double generateCoordinates() {
			return ((Math.random() < 0.5) ? 1.0 : -1.0) * Math.random() * 200.0;
		}
		
		static Target createTarget() {
			Target target = acquireTarget();
			target.x = generateCoordinates();
			target.y = generateCoordinates();
			target.radius = targetSize_normal;
			target.color = RED;
			return target;
		}
		
		static Target createTarget(double x, double y) {
			Target target = acquireTarget();
			target.x = x;
			target.y = y;
			target.radius = targetSize_normal;
			target.color = RED;
			return target;
		}
	
		
	}
	
	static Target[] pool;
	static Target acquireTarget() {
		for (int i = 0; i < pool.length; ++i) {
			if (!pool[i].inUse) {
				pool[i] = new Target();
				pool[i].inUse = true;
				return pool[i];
			}
		}
		PRINT("Error: Thing pool is completely full.");
		ASSERT(false);
		return null;
	}
	
	static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	static class CrossHair {
		
		double size;
		Color color;
		HitBox hitbox;
		double sensitivity = 0.2;
		
		//crosshair has a hitbox that stays constant as crosshair size changes - target hits determined by overlap of hitbox and target
		private class HitBox {
			double x;
			double y;
			int radius = 5;
		}
		
		public CrossHair(double size, Color color) {
			this.size = size;
			this.color = color;
			this.hitbox = new HitBox();
		}
		
		void draw() {
			double deltaX = mouseX - this.hitbox.x;
			double deltaY = mouseY - this.hitbox.y;
			this.hitbox.x = Math.floor(this.hitbox.x + deltaX * sensitivity);
			this.hitbox.y = Math.floor(this.hitbox.y + deltaY * sensitivity);
			// drawCircle(this.hitbox.x, this.hitbox.y, this.hitbox.radius, BLACK);
			drawRectangle(this.hitbox.x - size * 5, this.hitbox.y + size, this.hitbox.x + size * 5, this.hitbox.y - size, color);
			drawRectangle(this.hitbox.x - size, this.hitbox.y + size * 5, this.hitbox.x + size, this.hitbox.y - size * 5, color);
		}
		
		void setSize(double size) {
			this.size = size;
		}
		
		void setColor(Color color) {
			this.color = color;
		}
		
		void setSensitivity(double sens) {
			this.sensitivity = sens;
		}
		
		boolean hitCheck(Target target) {
			double distanceX = this.hitbox.x - target.x;
		    double distanceY = this.hitbox.y - target.y;
		    double distanceSquared = distanceX * distanceX + distanceY * distanceY;
		    double radiusSum = this.hitbox.radius + target.radius;
		    return distanceSquared < (radiusSum * radiusSum);
		}
	}
	
	static class button {
		Color color;
		double x;
		double y;
		String label;
		
		public button(Color color, double x, double y, String label) {
			this.color = color;
			this.x = x;
			this.y = y;
			this.label = label;
		}
		
		// main screen buttons are regular, setting toggle buttons are square
		void draw() {
			drawRectangle(this.x, this.y, this.x + 128, this.y + 32, this.color);
			drawString(this.label, this.x + 16, this.y + 8, BLACK, 32);
		}
		
		void draw(int fontSize, double x) {
			drawRectangle(this.x, this.y, this.x + 128, this.y + 32, this.color);
			drawString(this.label, x, this.y + 8, BLACK, fontSize);
		}
		
		void squareDraw(int fontSize) {
			drawRectangle(this.x, this.y, this.x + 32, this.y + 32, this.color);
			drawString(this.label, this.x + 8, this.y + 8, BLACK, fontSize);
		}
		
	}
	
	// main screen w/ title, buttons to access gamemodes, button to access settings
	static void MainScreen(button[] buttons) {
		drawString("COW.AIM", -128.0, 150.0, RED, 64);
		for (button b : buttons) {
			if (b.label.equals("Settings")) {
				b.draw(24, b.x + 8);
			} else {
				b.draw();
			}
		}
	}
	
	// target initialization for gamemodes
	static String setup(String label, HashMap<Double, Double> activeCoords) {
		if (label.equals("Flick")) {
			for (int i = 0; i < 6; i++) {
				Target target = Target.createTarget();
				// for each target, checks if it is close to other targets and redos coords if it is
				// should lead to generally wider spread
				for (double key: activeCoords.keySet()) {
					while (distance(key, activeCoords.get(key), target.x, target.y) < 15.0) {
						target.x = Target.generateCoordinates();
						target.y = Target.generateCoordinates();
					}
				}
				activeCoords.put(target.x, target.y);
			}
		}
		if (label.equals("Track")) {
			Target.createTarget(0, 32);
		}
		return label;
	}
	
	static int flick(CrossHair test, int score, HashMap<Double, Double> activeCoords) {
			boolean hitHappened = false;
			for (int i = 0; i < pool.length; ++i) {
				if (!pool[i].inUse) {
					continue;
				} else {
					if (mousePressed) {
						if (test.hitCheck(pool[i])) {
							hitHappened = true;
							pool[i].inUse = false;
							activeCoords.remove(pool[i].x);
							Target replacement = Target.createTarget(); // replenish hit target
							for (double key: activeCoords.keySet()) {
								while (distance(key, activeCoords.get(key), replacement.x, replacement.y) < 15.0) {
									replacement.x = Target.generateCoordinates();
									replacement.y = Target.generateCoordinates();
								}
							}
							activeCoords.put(replacement.x, replacement.y);
							score += 10; // score increases for every hit
						}
					}
				}
				pool[i].draw();
			}
			
			if (mousePressed && !hitHappened) { // if the user misses, decrease points
				score -= 5;
			}
		
			double eps = 16.0;
			drawString("score: " + score, 150.0 - eps, -256.0 + eps, BLACK, 16);

			return score;
		}
	
	static int[] track(CrossHair test, int score, double speed, int time, int interval, int direction) {
		// score points by keeping crosshair on target as it strafes back and forth
		boolean onTarget = false;
		if (test.hitCheck(pool[0])) {
			onTarget = true;
		}
		if (onTarget) {
			score++;
		}
		
		// reverse direction if near end of canvas
		if (pool[0].x >= 240.0 || pool[0].x <= -240.0) {
			direction = -direction;
		// randomly change or maintain direction at intervals - currently hardcoded at 1 second
		} else if ((time - interval) % interval == 0) {
			direction = (Math.random() > 0.5) ? 1 : -1;
		}
		
		pool[0].x += direction * speed;
		pool[0].draw();
		double eps = 16.0;
		drawString("score: " + score, 150.0 - eps, -256.0 + eps, BLACK, 16);
		return new int[] {score, direction};
	}
	
	// visual components of settings screen
	static void adjustSettings(button[] setting_buttons) {
		for (button b : setting_buttons) {
			if (b.label.equals("<") || b.label.equals(">") || b.label.equals("+") || b.label.equals("-")) {
				b.squareDraw(32);
			} else if (b.label.equals("s+") || b.label.equals("s-")) {
				b.squareDraw(20);
			} else {
				b.draw();
			}
		}
		drawString("TOGGLE COLOR", -80, 60, BLACK, 24);
		drawString("TOGGLE  SIZE", -80, -40, BLACK, 24);
		drawString("TOGGLE  SENS", -80, -140, BLACK, 24);
	}
	
	
	public static void main(String[] args) {
		HashMap<Double, Double> activeCoords = new HashMap<>();
		
		pool = new Target[12];
		for (int i = 0; i < pool.length; ++i) {
			pool[i] = new Target();
		}
		
		String gameMode = "";
		String queuedGameMode = "";
		int timeInTransition = 0; // time spent on transition screen before gamemode starts and on end screen after gamemode finishes
		int timeInGame = 0;
		CrossHair testHair = new CrossHair(1, CYAN);
		button[] main_buttons = {new button(RED, -64, 0, "Flick"), new button(RED, -64, -50, "Track"), new button(RED, -64, -100, "Settings")};
		button[] setting_buttons = {new button(RED, -200, -200, "exit"), new button(RED, -128, 50, "<"), 
									new button(RED, 96, 50, ">"), new button(RED, -128, -50, "-"), 
									new button(RED, 96, -50, "+"), new button(RED, 96, -150, "s+"),
									new button(RED, -128, -150, "s-")};
		Color[] colors = new Color[] {CYAN, BLACK, GREEN, YELLOW, BLUE, MAGENTA, ORANGE, PURPLE};
		List<Color> c = Arrays.asList(colors);
		int score = 0;
		
		// tracking gamemode specific variables
		final int track_changeInterval = 60;
		int track_direction = 1;
		
		while (beginFrame()) {
			// transition screen - displays a countdown before game starts
			if (gameMode.equals("Transition")) {
				timeInTransition++;
				if (timeInTransition >= 0 && timeInTransition <= 60) {
					drawString("3", -16.0, 150.0, BLACK, 64);
				} else if (timeInTransition > 60 && timeInTransition <= 120) {
					drawString("2", -16.0, 150.0, BLACK, 64);
				}  else if (timeInTransition <= 180) {
					drawString("1", -16.0, 150.0, BLACK, 64);
				} else {
					gameMode = queuedGameMode;
					timeInTransition = 0;
				}
			}
			
			// end screen - displays score then auto returns to main screen after 3 seconds
			else if (gameMode.equals("End")) {
				timeInTransition++;
				if (timeInTransition >= 180) {
					timeInTransition = 0;
					timeInGame = 0;
					for (int i = 0; i < pool.length; i++) {
						pool[i].inUse = false;
					}
					gameMode = "";
					queuedGameMode = "";
					score = 0;
				}
				drawString("Good Game!", -150, 100, BLACK, 48);
				drawString("Score: " + score, -220.0, 0, BLACK, 64);
				drawString("returning to main screen...", -200.0, -100, BLACK, 24);
			}
			
			else if (gameMode.equals("Flick")) {
				if (timeInGame >= 3600) {
					activeCoords = new HashMap<Double, Double>();
					gameMode = "End";
				}
				score = flick(testHair, score, activeCoords);
				testHair.draw();
				timeInGame++;
				continue;
			}
			
			else if (gameMode.equals("Track")) {
				if (timeInGame >= 3600) {
					gameMode = "End";
				}
				int[] outputs = track(testHair, score, 2, timeInGame, track_changeInterval, track_direction);
				track_direction = outputs[1];
				score = outputs[0];
				testHair.draw();
				timeInGame++;
				continue;
			}
			
			else if (gameMode.equals("")) {
				// button logic for main screen buttons - includes color change when hovering over
				for (button b: main_buttons) {
					if(testHair.hitbox.x < b.x + 100 && testHair.hitbox.x > b.x && testHair.hitbox.y < b.y + 50 && testHair.hitbox.y > b.y) {
						b.color = GREEN;
						if (mousePressed) {
							if (b.label.equals("Settings")) {
								gameMode = "Settings";
								queuedGameMode = "";
							} else {
								gameMode = "Transition";
								queuedGameMode = setup(b.label, activeCoords);
								break;
							}
						}
					} else {b.color = RED;}
				}
				MainScreen(main_buttons);
			}
			else if (gameMode.equals("Settings")) {
				for (button b: setting_buttons) {
					// button logic for setting change buttons
					if(testHair.hitbox.x < b.x + 100 && testHair.hitbox.x > b.x && testHair.hitbox.y < b.y + 50 && testHair.hitbox.y > b.y) {
						b.color = GREEN;
						if (mousePressed) {
							if(b.label.equals("+")) {
								testHair.setSize(testHair.size + 0.1);
							} else if (b.label.equals("-")) {
								testHair.setSize(testHair.size - 0.1);
							} else if (b.label.equals(">")) {
								int colorIndex = c.indexOf(testHair.color);
								if (colorIndex == c.size() - 1) {
									testHair.setColor(c.get(0));
								} else {testHair.setColor(c.get(colorIndex + 1));}
							} else if (b.label.equals("<")) {
								int colorIndex = c.indexOf(testHair.color);
								if (colorIndex == 0) {
									testHair.setColor(c.get(c.size() - 1));
								} else {testHair.setColor(c.get(colorIndex - 1));}
							} else if (b.label.equals("s+")) {
								if (testHair.sensitivity <= 0.95) {
									testHair.setSensitivity(testHair.sensitivity + 0.05);
								}
							} else if (b.label.equals("s-")) {
								if (testHair.sensitivity >= 0.10) {
									testHair.setSensitivity(testHair.sensitivity - 0.05);
								}
							} else if (b.label.equals("exit")) {
								gameMode = "";
							}
						}
					} else {b.color = RED;}
				}
				adjustSettings(setting_buttons);
			}
			testHair.draw();
		}	
	}
}

