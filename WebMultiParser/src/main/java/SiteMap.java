import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SiteMap {

    private Node head;

    public SiteMap(String root) {
        head = new Node(root);
    }

    public void addPath(String path) {
        if (path.toLowerCase().equals(head.getPathPart().toLowerCase()))
            return;

        if (path.contains(head.getPathPart()))
            path = path.substring(head.getPathPart().length());
        else
            return;
        List<String> splittedPath = new LinkedList<>(Arrays.asList(path.split("/")));
        addPath(splittedPath, head);
    }

    private void addPath(List<String> path, Node node) {
        if (path.get(0).equals(head.getPathPart()))
            path.remove(0);
        if (path.get(0).isEmpty()) {
            path.remove(0);
            addPath(path, node);
        }

        if (path.size() > 1) {
            Node child = node.findChildNode(path.get(0));
            if (child != null) {
                path.remove(0);
                addPath(path, child);
                return;
            } else {
                Node newChild = node.addChild(path.get(0));
                path.remove(0);
                addPath(path, newChild);
                return;
            }
        }

        Node child = node.findChildNode(path.get(0));
        if (child == null)
            node.addChild(path.get(0));
    }

    @Override
    public String toString() {
        int lvl = 0;
        StringBuilder str = new StringBuilder();
        return joinChildren(head, str, head.getPathPart(), lvl).toString();
    }

    private StringBuilder joinChildren(Node node, StringBuilder str, String lastPath, int lvl) {
        str.append(getPrefix(lvl) + lastPath);

        List<Node> children = node.getChildren();
        if (!children.isEmpty()) {
            for (Node child : node.getChildren()) {
                joinChildren(child, str, lastPath + "/" + child.getPathPart(), lvl + 1);
            }
        }
        return str;
    }

    private String getPrefix(int count) {
        StringBuilder res = new StringBuilder(count == 0 ? "" : "\n");
        for (int i = 0; i < count; i++)
            res.append("\t");
        return res.toString();
    }

    private class Node {
        private List<Node> children;
        private String pathPart;

        public Node(String pathPart) {
            this.pathPart = pathPart;
            children = new ArrayList<>();
        }

        public String getPathPart() {
            return pathPart;
        }

        public Node findChildNode(String child) {
            if (children.isEmpty())
                return null;
            else {
                for (Node node : children) {
                    if (node.getPathPart().equals(child))
                        return node;
                }
                return null;
            }
        }

        public Node addChild(String child) {
            Node newChild = new Node(child);
            children.add(newChild);
            return newChild;
        }

        public List<Node> getChildren() {
            return children;
        }
    }
}
