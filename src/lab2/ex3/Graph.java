package lab2.ex3;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lab2.ex3.JobshopScheduling.Machine;

import org.jacop.core.IntVar;

@SuppressWarnings("serial")
public class Graph extends JFrame {
	
	Machine[] m;

	public Graph(Machine[] m) {
		super("plotter");
		this.m = m;
		setSize(1500, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(new DrawingPane());
		
		setVisible(true);
	}
	
	
	private class DrawingPane extends JPanel {
		
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.BLACK);
			g.drawLine(20, 20, 20, getHeight()-40);
			g.drawLine(20, getHeight()-40, getWidth()-40, getHeight()-40);
			
			int frameHeight = (getHeight() - 40)/m.length-20;
			int widthMultiplyer = 20;
			
			Color[] colors = new Color[]{Color.pink,Color.cyan,Color.blue,Color.green,Color.red,Color.magenta};
			
			
			for (int i = 0; i < m.length; i++) {
				g.drawString(""+i, 5, frameHeight*(i+1)-frameHeight/2);
				
				for (IntVar[] rectangle : m[i].rect.getRectangles()) {
					int x = rectangle[0].value();
					int y = rectangle[1].value();
					int width = rectangle[2].value();
					int height = rectangle[3].value();
					
					String text = rectangle[0].id();
					
					System.out.println(text.substring(1,2));
					
					int color = Integer.parseInt(text.substring(1,2));
					g.setColor(colors[color-1]);
					g.drawRect(x*widthMultiplyer+20, frameHeight*i+20, width*widthMultiplyer, frameHeight-20);
					g.setColor(Color.black);
					g.drawString(text, x*widthMultiplyer+30, frameHeight*i+60);
					
				}
				
				
				
			}
			
		
		}
		
	}
	
}
