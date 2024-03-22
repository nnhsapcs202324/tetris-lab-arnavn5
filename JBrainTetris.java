import java.awt.Button;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 * Write a description of class JBrainTetris here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class JBrainTetris extends JTetris
{
    private Brain brain;
    private JButton enableBrain;
    private boolean isBrainEnabled = false;
    private int count;
    private Move bestMove;
    
    public JBrainTetris( int width, int height)
    {
        super(width,height);
        
    }
    
    @Override
    public Container createControlPanel() {
    Container pan = super.createControlPanel();
    ClickListener listener = new ClickListener();
    
    JComboBox<Brain> comboBox1 = new JComboBox<>();
    ArrayList<Brain> list = BrainFactory.createBrains();
    Brain[] brains = new Brain[list.size()];
    for (int i = 0; i < brains.length; i++) {
        brains[i] = list.get(i);
    }
    comboBox1 = new JComboBox<>(brains);
    brain = new SimpleBrain();
    pan.add(comboBox1);
    
    enableBrain = new JButton("Enable Brain");
    pan.add(enableBrain);
    enableBrain.addActionListener(listener);
    
    return pan;
}
public class ComboBox implements ActionListener
{
    public void actionPerformed(ActionEvent a)
    {
        JComboBox comboBox2 = (JComboBox)a.getSource();
        brain = (Brain)comboBox2.getSelectedItem();
    }
}
public class ClickListener implements ActionListener
{
    public void actionPerformed(ActionEvent a)
    {
        if(a.getSource() == enableBrain)
        {
            count ++;
            if(count % 2 != 0)
            {
                isBrainEnabled = true;
                enableBrain.setText("Disable Brain");
            }
            else{
                isBrainEnabled = false;
                enableBrain.setText("Enable Brain");
            }
        }
    }
}
@Override public Piece pickNextPiece()
{
    int pieceNumber = (int)(this.pieces.length * this.random.nextDouble());
    int height = HEIGHT + TOP_SPACE;
    this.bestMove = brain.bestMove(this.board, this.pieces[pieceNumber], height);
    return this.pieces[pieceNumber];
}
@Override
public void tick(int verb) {
    if (isBrainEnabled) {
        if (this.currentX < bestMove.getX()) {
            super.tick(RIGHT);
        } else if (this.currentX > bestMove.getX()) {
            super.tick(LEFT);
        }
        if (!this.currentPiece.nextRotation().equals(bestMove.getPiece().nextRotation())) {
            super.tick(ROTATE);
        }
        if (this.currentPiece.nextRotation().equals(bestMove.getPiece().nextRotation()) && this.currentX == bestMove.getX()) {
            super.tick(DROP);
            super.tick(verb);
        }
    }
        // Execute default behavior when brain is disabled
        super.tick(verb);
    }
}






