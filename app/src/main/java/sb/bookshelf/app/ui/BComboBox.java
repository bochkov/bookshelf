package sb.bookshelf.app.ui;

import javax.swing.*;
import java.util.Collection;

public final class BComboBox<T> extends JComboBox<T> {

    private final Class<T> itemClass;

    public BComboBox(boolean editable, Class<T> itemClass) {
        this.itemClass = itemClass;
        setEditable(editable);
    }

    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    public void addItems(Collection<T> items, boolean addFirstNull) {
        if (addFirstNull) {
            addItem(null);
        }
        addItems(items);
    }

    @Override
    public T getSelectedItem() {
        return itemClass.cast(super.getSelectedItem());
    }

    public void select(T item) {
        for (int i = 0; i < getModel().getSize(); ++i) {
            if (item != null && getItemAt(i) != null && getItemAt(i).equals(item)) {
                setSelectedItem(getItemAt(i));
                return;
            }
        }
        addItem(item);
        setSelectedItem(item);
    }

}
