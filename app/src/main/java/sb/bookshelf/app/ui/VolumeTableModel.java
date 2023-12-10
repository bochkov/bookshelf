package sb.bookshelf.app.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import sb.bookshelf.common.model.Volume;

public final class VolumeTableModel extends AbstractTableModel {

    private final List<Volume> volumes = new ArrayList<>();

    public List<Volume> allItems() {
        return new ArrayList<>(volumes);
    }

    public void set(List<Volume> volumes) {
        this.volumes.clear();
        this.volumes.addAll(volumes);
        fireTableDataChanged();
    }

    public void add(Volume volume) {
        this.volumes.add(volume);
        fireTableDataChanged();
    }

    public void put(int pos, Volume volume) {
        this.volumes.add(pos, volume);
        fireTableDataChanged();
    }

    public Volume get(int idx) {
        if (idx < 0 || idx > volumes.size())
            return null;
        return volumes.get(idx);
    }

    public Volume getById(String id) {
        for (Volume vol : volumes)
            if (id.equals(vol.getId()))
                return vol;
        return null;
    }

    public void removeIds(List<String> ids) {
        volumes.removeIf(v -> ids.contains(v.getId()));
        fireTableDataChanged();
    }

    public void replaceIds(Volume volume) {
        int idx = idxOf(volume);
        if (idx >= 0) {
            volumes.set(idx, volume);
            fireTableDataChanged();
        }
    }

    private int idxOf(Volume volume) {
        return idxOf(volume.getId());
    }

    private int idxOf(String id) {
        for (var i = 0; i < volumes.size(); ++i) {
            if (id.equals(volumes.get(i).getId()))
                return i;
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return volumes.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var vol = volumes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> vol.getAuthor();
            case 1 -> vol.getTitle();
            default -> null;
        };
    }
}
