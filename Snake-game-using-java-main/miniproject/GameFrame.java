package miniproject;



import java.awt.Toolkit;
import javax.swing.JFrame;
public class GameFrame extends JFrame{

	GameFrame(String name){
                this.setSize(1400,800);
		this.add(new GamePanel(name));
		this.setTitle("Snake");
    		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.pack();
                
		this.setVisible(true);
		this.setLocationRelativeTo(null);
                this.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\Documents\\NetBeansProjects\\JavaApplication3\\src\\snake.png")); 


	}
        
        //public static void main(String args[]) {
    //new GameFrame(); 
    //}
}

        