package sb.bookshelf.app;

import java.util.Objects;
import javax.swing.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public final class Images {

    public static final ImageIcon LOGO = new ImageIcon(
            Objects.requireNonNull(App.class.getResource("/bookshelf.png"))
    );

    public static final FlatSVGIcon ICON_ADD = new FlatSVGIcon("svg/plus-solid.svg", 12, 12);
    public static final FlatSVGIcon ICON_DEL = new FlatSVGIcon("svg/minus-solid.svg", 12, 12);
    public static final FlatSVGIcon ICON_EDIT = new FlatSVGIcon("svg/pen-solid.svg", 12, 12);
    public static final FlatSVGIcon ICON_FIND = new FlatSVGIcon("svg/search-solid.svg", 12, 12);
    public static final FlatSVGIcon ICON_CLEAR = new FlatSVGIcon("svg/eraser-solid.svg", 12, 12);


    private Images() {
    }
}
