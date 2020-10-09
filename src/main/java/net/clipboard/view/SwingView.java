package net.clipboard.view;

import net.clipboard.controller.Transfer;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class SwingView {
    private final Transfer transfer;
    private final JFrame mainWindow;
    private final JPanel panel;
    private final Map<Integer, JLabel> displays;

    public SwingView(Transfer transfer) {
        this.transfer = transfer;
        this.mainWindow = new JFrame("Multi-clipboard");
        this.panel = new JPanel();
        this.displays = new HashMap<>();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void create() {
        mainWindow.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        mainWindow.add(panel, constraints);
        panel.setLayout(new GridBagLayout());

        addTitleRow();
        for (int i = 0; i < 10; i++) {
            addClipRow(i);
        }
        applyKeyBindings();
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    private void addTitleRow() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Store"), constraints);
        constraints.gridx = 2;
        panel.add(new JLabel("Recall"), constraints);
    }

    private void addClipRow(final int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1+row;
        JButton store = new JButton(String.format("Store in slot %1$d", constraints.gridy));
        panel.add(store, constraints);

        constraints.gridx = 1;
        JLabel label = new JLabel("<Empty>");
        displays.put(row, label);
        panel.add(label, constraints);

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
        panel.add(recall, constraints);
    }

    private void applyKeyBindings() {
        for (int i = 0; i < 10; i++) {
            KeyStroke copyKeyStroke = KeyStroke.getKeyStroke(String.format("ctrl %1$d", i));
            System.out.println(String.format("Registered copy against keystroke %1$s", copyKeyStroke));
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(copyKeyStroke, "pressed");
            panel.getActionMap().put(copyKeyStroke, new CopyAction(transfer, i == 0 ? 10 : i));

            KeyStroke pasteKeyStroke = KeyStroke.getKeyStroke(String.format("alt %1$d", i));
            System.out.println(String.format("Registered paste against keystroke %1$s", pasteKeyStroke));
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(pasteKeyStroke, "pressed");
            panel.getActionMap().put(pasteKeyStroke, new PasteAction(transfer, i == 0 ? 10 : i));
        }
    }

    private static class CopyAction implements Action {
        private final Transfer transfer;
        private final int index;
        private boolean enabled = false;

        public CopyAction(Transfer transfer, int index) {
            this.transfer = transfer;
            this.index = index;
        }

        @Override
        public Object getValue(String key) {
            return null;
        }

        @Override
        public void putValue(String key, Object value) {

        }

        @Override
        public void setEnabled(boolean b) {
            enabled = b;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(String.format("Key stroke copying from clipboard to %1$d", index));
            Transferable transferable = transfer.getTransferableFromClipboard();
            transfer.saveToSlot(index, transferable);
        }
    }

    private static class PasteAction implements Action {
        private final Transfer transfer;
        private final int index;
        private boolean enabled = false;

        public PasteAction(Transfer transfer, int index) {
            this.transfer = transfer;
            this.index = index;
        }

        @Override
        public Object getValue(String key) {
            return null;
        }

        @Override
        public void putValue(String key, Object value) {

        }

        @Override
        public void setEnabled(boolean b) {
            enabled = b;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(String.format("Key stroke copying to clipboard from %1$d", index));
            Transferable transferable = transfer.loadFromSlot(index);
            transfer.setTransferableToClipboard(transferable);
        }
    }
}
