package edu.mccc.cos210.fp2014.col.client.objects;
import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileInputStream;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class ShapeRecognizer {
	private int[] x;
	private int[] y;
	private BufferedImage[] digitPictures;
	private int[][][] digitArrays = new int[10][5][5];
	private double[] scores;
	private BufferedImage currentDigit;
	public ShapeRecognizer() {
		digitPictures = new BufferedImage[9];
		scores = new double[10];
			digitArrays[0] = this.zero;
			digitArrays[1] = this.two;
			digitArrays[2] = this.three;
			digitArrays[3] = this.four;
			digitArrays[4] = this.five;
			digitArrays[5] = this.six;
			digitArrays[6] = this.seven1;
			digitArrays[7] = this.eight;
			digitArrays[8] = this.nine;
			digitArrays[9] = this.seven2;
		getImages();
	}
	
	private void getImages() {
		try {
			digitPictures[0] = ImageIO.read(
			getClass().getResource("../assets/digit_zero.jpg"));
			digitPictures[1] = ImageIO.read(
			getClass().getResource("../assets/digit_two.jpg"));
			digitPictures[2] = ImageIO.read(
			getClass().getResource("../assets/digit_three.jpg"));
			digitPictures[3] = ImageIO.read(
			getClass().getResource("../assets/digit_four.jpg"));
			digitPictures[4] = ImageIO.read(
			getClass().getResource("../assets/digit_five.jpg"));
			digitPictures[5] = ImageIO.read(
			getClass().getResource("../assets/digit_six.jpg"));
			digitPictures[6] = ImageIO.read(
			getClass().getResource("../assets/digit_seven.jpg"));
			digitPictures[7] = ImageIO.read(
			getClass().getResource("../assets/digit_eight.jpg"));
			digitPictures[8] = ImageIO.read(
			getClass().getResource("../assets/digit_nine.jpg"));
		} catch (IOException e) {
			//Image loading error message
		}
	}

	public int[] recognizeShape(ArrayList<Integer> xPoints,
							ArrayList<Integer> yPoints) {
		int myAnswer = -1;
		// FIRST WE MUST FIND OUT HOW BIG THE DRAWN SHAPE IS
		// AND THEN SCALE IT TO A 1 X 1 BOX
		int[] x = new int[xPoints.size()];
		int[] y = new int[yPoints.size()];
		for (int i = 0; i < xPoints.size(); i++) {
			x[i] = xPoints.get(i);
		} for (int j = 0; j < yPoints.size(); j++) {
			y[j] = yPoints.get(j);
		}
		this.x = x;
		this.y = y;
		//find the smallest x
		int smallestX = 100000000;
		for (int i = 0; i < x.length; i++) {
			if (x[i] < smallestX) {
				smallestX = x[i];
			}
		}
		//find the largest x
		int largestX = -100000000;
		for (int i = 0; i < x.length; i++) {
			if (x[i] > largestX) {
				largestX = x[i];
			}
		}
		//find the smallest y
		int smallestY = 100000000;
		for (int i = 0; i < y.length; i++) {
			if (y[i] < smallestY) {
				smallestY = y[i];
			}
		}
		//find the largest y
		int largestY = -100000000;
		for (int i = 0; i < y.length; i++) {
			if (y[i] > largestY) {
				largestY = y[i];
			}
		}
		
		int shapeWidth = largestX - smallestX;
		int shapeHeight = largestY - smallestY;
		double scaleX = 5.0 / (double)shapeWidth;
		double scaleY = 5.0 / (double)shapeHeight;
		double xTranslation = smallestX * scaleX;
		double yTranslation = smallestY * scaleY;
		
		//Now scale all the points so they're in a 1x1 box
		double[] xp = new double[x.length];
		double[] yp = new double[y.length];
		int[][] drawnPoints = new int[5][5];
		int numPoints = xp.length;
		
		//If the width is very narrow assume it's a "1"
		if (shapeWidth < 10) {
			myAnswer = 1;
		}
		else {
			int pointCount = 0;
			//Put the points in a 5x5 box
			for (int i = 0; i < xp.length; i++) {
				//X
				xp[i] = x[i] * scaleX - xTranslation;
				//Add a buffer to compensate for computation error
				if (xp[i] < .01) {
					xp[i] = .01;
				}
				if (xp[i] > 4.99) {
					xp[i] = 4.99;
				}
				//Y
				yp[i] = y[i] * scaleY - yTranslation;
				//Add a buffer to compensate for computation error
				if (yp[i] < .01) {
					yp[i] = .01;
				}
				if (yp[i] > 4.99) {
					yp[i] = 4.99;
				}
				int newY = (int) yp[i];
				int newX = (int) xp[i];
				if (drawnPoints[newY][newX] != 1) {
					drawnPoints[newY][newX] = 1;
					pointCount += 1;
				}
			}
			// Now assign a grade for each digit		
			int hits;
			int denominator;
			for (int i = 0; i < drawnPoints.length; i++) {
				for (int j = 0; j < 5; j++) {
					System.out.print(drawnPoints[i][j] + " ");
				}
				System.out.print("\n");
			}
			System.out.println(" ");			
			for(int i = 0; i < 10; i++) {
				//See how many hit points are in this digit
				denominator = 0;
				hits = 0;
				int[][] currentD = digitArrays[i];
				for(int j = 0; j < 5; j++) {
					for(int k = 0; k < 5; k++) {
						//Rewarded for staying in bounds
						if (currentD[j][k] == 1) {
							if (drawnPoints[j][k] == 1) {
								hits += 1;
							}
							denominator += 1;
						} 
					}
				}
				//Score what the user drew
				double score = ((double) hits / denominator)
							* ((double) hits / pointCount);
				if (i < 1) {
					System.out.println("likelihood of " + i +
									" is: " + score);
					scores[i] = score;
				}
				else {
					if (i < 9) {
					System.out.println("likelihood of " + (i + 1) 
									+ " is: " + score);
					scores[i] = score;
					} else {
						System.out.println("likelihood of alternative version of a 7 is: " + score);
						scores[i] = score;
					}
				}
			}
			
			/*
			for (int i = 0; i < digitPictures.length; i++) {
				currentDigit = digitPictures[i];
				int w = currentDigit.getWidth();
				int h = currentDigit.getHeight();
				//Do scoring here
				for (int j = 0; j < numPoints; j++) {
						double currentX = xp[j];
						double currentY = yp[j];
						double searchX = currentX * w;
						double searchY = currentY * h;
						System.out.println("x: " + currentX);
						System.out.println("y: " + currentY);
						System.out.println("sx: " + searchX);
						System.out.println("sy: " + searchY);
						Color pixelColor = new Color(
							currentDigit.getRGB((int)searchX,
												(int)searchY)
												);
						double rComp = (double)pixelColor.getRed();
						double gComp = (double)pixelColor.getGreen();
						double bComp = (double)pixelColor.getBlue();
						double brightness = (rComp / 255 
										   + gComp / 255
										   + bComp / 255) / 3;
						if (brightness <= 0.1) {
							hits += 1;
						}
				}
				scores[i] = (double) hits / numPoints;
				hits = 0;
			}
			for (int i = 0; i < scores.length; i++) {
				if (i < 1)
				System.out.println("likelihood of " + i + " is: " + scores[i]);
				else
				System.out.println("likelihood of " + (i + 1) + " is: " + scores[i]);
			}
			*/
			double highestScore = -100.0;
			int returnIndex = 0;
			for ( int i = 0; i < scores.length; i++) {
				if(scores[i] > highestScore) {
					highestScore = scores[i];
					returnIndex = i;
				}
			}
			if (returnIndex < 1) {
				myAnswer = returnIndex;
			} else {
				if (returnIndex != 9) {
					myAnswer = returnIndex + 1;
				} else {
					myAnswer = 7;
				}
			}	
		}
		int[] reply = new int[4];
		reply[0] = myAnswer;
		reply[1] = smallestX + (shapeWidth / 2);
		reply[2] = smallestY + (shapeHeight / 2);
		reply[3] = shapeHeight;
		return reply;
	}
	
	private int[][] zero = new int[][] {
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 1},
	{1, 0, 0, 0, 1},
	{1, 0, 0, 0, 1},
	{1, 1, 1, 1, 1}
	};
	
	private int[][] one = new int[][] {
	{0, 0, 1, 0, 0},
	{0, 0, 1, 0, 0},
	{0, 0, 1, 0, 0},
	{0, 0, 1, 0, 0},
	{0, 0, 1, 0, 0}
	};
	
	private int[][] two = new int[][] {
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 1},
	{0, 0, 0, 1, 0},
	{0, 1, 1, 0, 0},
	{1, 1, 1, 1, 1}
	};
	
	private int[][] three = new int[][] {
	{1, 1, 1, 1, 1},
	{0, 0, 0, 0, 1},
	{0, 0, 1, 1, 1},
	{0, 0, 0, 0, 1},
	{1, 1, 1, 1, 1}
	};
	
	private int[][] four = new int[][] {
	{1, 0, 0, 0, 1},
	{1, 0, 0, 0, 1},
	{1, 1, 1, 1, 1},
	{0, 0, 0, 0, 1},
	{0, 0, 0, 0, 1}
	};
	
	private int[][] five = new int[][] {
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 0},
	{1, 1, 1, 1, 1},
	{0, 0, 0, 0, 1},
	{1, 1, 1, 1, 1}
	};
	
	private int[][] six = new int[][] {
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 0},
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 1},
	{1, 1, 1, 1, 1}
	};
	
	private int[][] seven1 = new int[][] {
	{1, 1, 1, 1, 1},
	{0, 0, 0, 1, 0},
	{0, 0, 0, 1, 0},
	{0, 0, 1, 0, 0},
	{0, 0, 1, 0, 0}
	};
	
	private int[][] seven2 = new int[][] {
	{1, 1, 1, 1, 1},
	{0, 0, 0, 0, 1},
	{0, 0, 0, 0, 1},
	{0, 0, 0, 0, 1},
	{0, 0, 0, 0, 1}
	};
	
	private int[][] eight= new int[][] {
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 1},
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 1},
	{1, 1, 1, 1, 1}
	};
	
	private int[][] nine= new int[][] {
	{1, 1, 1, 1, 1},
	{1, 0, 0, 0, 1},
	{1, 1, 1, 1, 1},
	{0, 0, 0, 0, 1},
	{0, 0, 0, 0, 1}
	};
}