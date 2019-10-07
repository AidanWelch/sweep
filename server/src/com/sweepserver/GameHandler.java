package com.sweepserver;

import java.util.*;

public class GameHandler extends Thread{

	public byte[][] map;
	public byte[][] colormap;
	public String[] colors;
	
	public GameHandler() {
		this.map = mapGen();
		this.colormap = new byte[33][33];
		this.colors = new String[5];
	}
	
	public int getColor(String ip) {
		int color = 1;
		for(; color < 5; color++) {
			if(colors[color] == null) {
				colors[color] = ip;
				return color;
			}
		}
		return 0;
	}
	
	public int findColor(String ip) {
		int color = 1;
		for(; color < 5; color++) {
			if(colors[color].equals(ip)) {
				return color;
			}
		}
		return 0;
	}
	
	public boolean[][] getColorTiles(int color) {
		boolean[][] colorTiles = new boolean[33][33];
		for(int x = 0; x < 33; x++) {
			for(int y = 0; y<33; y++) {
				if(colormap[x][y] == color) {
					colorTiles[x][y] = true;
				} else {
					colorTiles[x][y] = false;
				}
			}
		}
		return colorTiles;
	}
	
	public int getColorTilesCount(int color) {
		boolean[][] tiles = getColorTiles(color);
		int ntiles = 0;
		for(int x = 0; x < 33; x++) {
			for(int y = 0; y<33; y++) {
				if(tiles[x][y]) {
					ntiles++;
				}
			}
		}
		return ntiles;
	}
	
	public void deleteColor(int color) {
		if(color != 0) {
			//colors[color] = null;
			for (int x = 0; x < 33; x++) {
				for (int y = 0; y < 33; y++) {
					if (colormap[x][y] == (byte)color) {
						colormap[x][y] = 0;
					}
				}
			}
		}
	}
	
	private void refreshBoard() {
		for(int i = 1; i < 5; i++) {
			deleteColor(i);
		}
		map = mapGen();
	}
	
	//loss or byte array of visible
	public String onClick(int x, int y, int color) {
		if(color != 0 && colormap[x][y] == 0) {
			if(map[x][y] == 9) {
				deleteColor(color);
				return getVisible(color);
			} else {
				colormap[x][y] = (byte)color;
				//below chunk calculates if any tiles are adjacent to 0s and reveals them if they are
				if(map[x][y] == 0) {
					
					for(int a = 0; a < 33; a++) {
						for(int b = 0; b < 33; b++) {
							for(int la = -1; la <= 1; la++) {
								if((a+la) >= 0 && (a+la) < 33) {
									for(int lb = -1; lb <= 1; lb++) {
										if((b+lb) >= 0 && (b+lb) < 33) {
											if(map[a+la][b+lb] == 0 && colormap[a+la][b+lb] == color) {
												colormap[a][b] = (byte)color;
											}
										}
									}
								}
							}
						}
					}
					
				}
				
				return getVisible(color);
			}
		} else {
			return getVisible(color);
		}
	}
	
	public String getVisible(int color) {
		boolean[][] colorTiles = getColorTiles(color);
		byte[][] revealedTiles = new byte[33][33];
		for(int x = 0; x < 33; x++) {
			for(int y = 0; y < 33; y++) {
				if(colorTiles[x][y]) {
					revealedTiles[x][y] = map[x][y];
				} else {
					revealedTiles[x][y] = 9;
				}
			}
		}
		return Arrays.deepToString(revealedTiles);
	}
	
	
	private byte[][] mapGen() {
		
		Random rand = new Random();
		//none(0-8), bomb(9)
		byte[][] map = new byte[33][33];
		
		int nbombs = rand.nextInt(100) + 100;
		
		for(int i = 0; i < nbombs; i++) {
			int x = rand.nextInt(33);
			int y = rand.nextInt(33);
			while(map[x][y] == 9) {
				x = rand.nextInt(33);
				y = rand.nextInt(33);
			}
			map[x][y] = 9;
		}
		
		for(int x = 0; x < 33; x++) {
			for(int y = 0; y < 33; y++) {
				if(map[x][y] != 9) {
					byte count = 0;
					
					for(int lx = -1; lx <= 1; lx++) {
						if((x+lx) >= 0 && (x+lx) < 33) {
							for(int ly = -1; ly <= 1; ly++) {
								if((y+ly) >= 0 && (y+ly) < 33) {
									if(map[x+lx][y+ly] == 9) {
										count += 1;
									}
								}
							}
						}
					}
					map[x][y] = count;
				}
			}
		}
		
		return map;
	}

}
