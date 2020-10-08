package net.clipboard.view;

import net.clipboard.controller.Transfer;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SwingView {
    private final Transfer transfer;
    private final JFrame mainWindow;
    private final Map<Integer, JLabel> displays;

    public SwingView(Transfer transfer) {
        this.transfer = transfer;
        this.mainWindow = new JFrame("Multi-clipboard");
        this.displays = new HashMap<>();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void create() {
        mainWindow.setLayout(new GridBagLayout());

        addTitleRow();
        for (int i = 0; i < 10; i++) {
            addClipRow(i);
        }
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    private void addTitleRow() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainWindow.add(new JLabel("Store"), constraints);
        constraints.gridx = 2;
        mainWindow.add(new JLabel("Recall"), constraints);
    }

    private void addClipRow(final int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1+row;
        JButton store = new JButton(String.format("Store in slot %1$d", constraints.gridy));
        mainWindow.add(store, constraints);

        constraints.gridx = 1;
        JLabel label = new JLabel("<Empty>");
        displays.put(row, label);
        mainWindow.add(label, constraints);

        store.addActionListener(new ActionListener() {
            private final int myRow = row;
            @Override
            public void actionPerformed(ActionEvent e) {
                Transferable data = transfer.getTransferableFromClipboard();
                transfer.saveToSlot(myRow, data);
            }
        });

        constraints.gridx = 2;
        JButton recall = new JButton(String.format("Recall from slot %1$d", constraints.gridy));
        recall.addActionListener(new ActionListener() {
            private final int myRow = row;
            @Override
            public void actionPerformed(ActionEvent e) {
                Transferable data = transfer.loadFromSlot(myRow);
                transfer.setTransferableToClipboard(data);
            }
        });
        mainWindow.add(recall, constraints);
    }
}
