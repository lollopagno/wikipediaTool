package views.components;

import javax.swing.*;
import java.util.Optional;
import java.util.Set;

public class SequenceSolutions extends JTable {
    private Set<PlayerSolution> data;

    public SequenceSolutions() {
    }

    public void setSolution(PlayerSolution solution) {
        Optional<PlayerSolution> item = this.data.stream().filter(f -> f.getName().equals(solution.getName())).findFirst();
        if(item.isPresent()) {
            item.get().setSequence(solution.getSequence());
            item.get().setRightNumbers(solution.getRightNumbers());
            item.get().setRightPlacedNumbers(solution.getRightPlacedNumbers());
        } else {
            data.add(solution);
        }
    }
}
