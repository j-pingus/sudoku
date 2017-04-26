package gee.sudoku.solver;

import gee.sudoku.krn.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerald on 26/04/17.
 */
public class YWing implements Strategy {
    @Override
    public MatriceAction getAction(Matrice matrice) {
        CellReference pairs[] = matrice.getPairs();
        for (Cell c : matrice.getPairs()) {
            for (YWingReference yWingRef : getChained(matrice, c, pairs)) {
                Cell a = matrice.getCell(new CellReference(yWingRef.left.getRow(), yWingRef.right.getCol()));
                Cell b = matrice.getCell(new CellReference(yWingRef.right.getRow(), yWingRef.left.getCol()));
                MatriceZone z = new MatriceZone("Y-Wing");
                z.addCell(c);
                z.addCell(yWingRef.left);
                z.addCell(yWingRef.right);
                MatriceAction action = new MatriceAction(Strategies.Y_WING, z);
                action.setHintValues(new int[]{yWingRef.choice});
                if (a.hasChoice(yWingRef.choice)) {
                    z.addCell(a);
                    action.removeChoices(matrice,a,yWingRef.choice);
                }
                if (b.hasChoice(yWingRef.choice)) {
                    z.addCell(a);
                    action.removeChoices(matrice,b,yWingRef.choice);
                }
                if(action.size()>0){
                    return action;
                }
            }
        }
        return null;
    }

    private List<YWingReference> getChained(Matrice matrice, Cell cell, CellReference[] pairs) {
        List<YWingReference> YWingReferenceList = new ArrayList<>();
        Integer choices[] = cell.getChoices();
        for (CellReference leftReference : pairs) {
            if (!leftReference.equals(cell) && leftReference.isConnected(cell)) {
                Cell left = matrice.getCell(leftReference);
                if (left.hasChoice(choices[0])) {
                    int otherChoice = -1;
                    for (int choice : left.getChoices()) {
                        if (choice != choices[0]) {
                            otherChoice = choice;
                        }
                    }
                    if (otherChoice != choices[1]) {
                        for (CellReference rightReference : pairs) {
                            if (!leftReference.equals(cell) && !rightReference.equals(cell) &&
                                    !rightReference.equals(leftReference) && rightReference.isConnected(cell)) {
                                Cell right = matrice.getCell(rightReference);
                                if (right.hasChoices(choices[1], otherChoice) && left.getCol() != right.getCol()
                                        && left.getRow() != right.getRow()) {
                                    YWingReference ywing = new YWingReference();
                                    ywing.left = left;
                                    ywing.right = right;
                                    ywing.choice = otherChoice;
                                    YWingReferenceList.add(ywing);
                                }
                            }
                        }
                    }
                }
            }
        }
        return YWingReferenceList;
    }

    class YWingReference {
        Cell left;
        Cell right;
        int choice;
    }


}
