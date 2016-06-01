package renatoprobst.wearsender.listener;

import com.google.android.gms.wearable.Node;

import java.util.List;

public interface NodeChangeListener {

    void onReceiveNodes(final List<Node> nodes);
}
