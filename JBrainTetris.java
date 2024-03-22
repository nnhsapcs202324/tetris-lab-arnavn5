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
    Container panel = super.createControlPanel();
    ClickListener listener = new ClickListener();
    
    JComboBox<Brain> cb = new JComboBox<>();
    ArrayList<Brain> brainsList = BrainFactory.createBrains();
    Brain[] brains = new Brain[brainsList.size()];
    for (int i = 0; i < brains.length; i++) {
        brains[i] = brainsList.get(i);
    }
    cb = new JComboBox<>(brains);
    brain = new SimpleBrain();
    cb.addActionListener(listener); // Corrected method name
    panel.add(cb);
    
    enableBrain = new JButton("Enable Brain"); // No need for "this"
    panel.add(enableBrain);
    enableBrain.addActionListener(listener);
    
    return panel;
}
public class ComboBox implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        JComboBox cb = (JComboBox)e.getSource();
        brain = (Brain)cb.getSelectedItem();
    }
}
public class ClickListener implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == enableBrain)
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
    int pieceNum = (int)(this.pieces.length * this.random.nextDouble());
    int limitHeight = HEIGHT + TOP_SPACE;
    this.bestMove = brain.bestMove(this.board, this.pieces[pieceNum], limitHeight);
    return this.pieces[pieceNum];
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






