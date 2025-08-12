package shiyee_FYP.fullstack_backend.Utils;

import org.jgrapht.Graph;

public class GraphUtils {

    /**
     * 计算有向图密度
     * 密度 = 实际边数 / 最大可能边数 (n*(n-1))
     */
    public static double calculateDensity(Graph<?, ?> graph) {
        int vertexCount = graph.vertexSet().size();
        if (vertexCount < 2) {
            return 0.0;
        }
        return (double) graph.edgeSet().size() / (vertexCount * (vertexCount - 1));
    }

    /**
     * 计算平均节点度数
     */
    public static double calculateAverageDegree(Graph<?, ?> graph) {
        if (graph.vertexSet().isEmpty()) {
            return 0.0;
        }
        return (double) graph.edgeSet().size() * 2 / graph.vertexSet().size();
    }
}