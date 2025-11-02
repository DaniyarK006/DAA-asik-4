package com.smartcity.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GraphLoader {

    public static GraphData loadFromFile(String filepath) throws IOException, JSONException {
        String content = Files.readString(Path.of(filepath));
        JSONObject json = new JSONObject(content);
        boolean directed = json.optBoolean("directed", false);
        int n = json.getInt("n");
        String weightModel = json.optString("weight_model", "edge");
        int source = json.optInt("source", 0);

        Graph graph = new Graph(n, directed, weightModel);
        JSONArray edges = json.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject edge = edges.getJSONObject(i);
            int u = edge.getInt("u");
            int v = edge.getInt("v");
            int w = edge.optInt("w", 1); // по умолчанию вес 1
            graph.addEdge(u, v, w);
        }

        return new GraphData(graph, source);
    }

    public record GraphData(Graph graph, int source) {
    }
}
