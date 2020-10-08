package net.clipboard.controller;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.Map;

public class Transfer {
    private final Map<Integer, Transferable> records;

    public Transfer(Map<Integer, Transferable> records) {
        this.records = records;
    }

    public Transferable getTransferableFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return clipboard.getContents(null);
    }

    public void setTransferableToClipboard(Transferable transferable) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(transferable, null);
    }

    public Transferable loadFromSlot(int slot) {
        return records.get(slot);
    }

    public void saveToSlot(int slot, Transferable transferable) {
        records.put(slot, transferable);
    }
}
