package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDebug;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.geom.PgVertexStar;
import jvx.project.PjWorkshop;
import sun.java2d.pipe.SpanShapeRenderer;
import util.Util;

import java.util.ArrayList;

/**
 * Created by Immortaly007 on 20-5-2015.
 */
public class Task3IterativeAveraging extends PjWorkshop {
    PgElementSet m_geom;
    PgElementSet m_geomSave;

    public Task3IterativeAveraging() {
        super("Task 3 (Iterative Averaging)");
        init();
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom 		= (PgElementSet)super.m_geom;
        m_geomSave 	= (PgElementSet)super.m_geomSave;
    }

    public void init() {
        super.init();
    }

    public void apply(double stepwidth)
    {
        // For each vertex, calculate the average position of it's neighbors
        // Then subtract this average from the current vertex position
        SimpleVertexStar[] stars =  EverythingHelper.makeVertexStars(m_geom);
        PdVector[] vertexOffsets = new PdVector[m_geom.getVertices().length];
        for (SimpleVertexStar star : stars)
        {
            PdVector center = star.getCenter();
            ArrayList<Double> xCoords = new ArrayList<Double>();
            ArrayList<Double> yCoords = new ArrayList<Double>();
            ArrayList<Double> zCoords = new ArrayList<Double>();
            for (PdVector vertex : star.getNeighborVertices()) {
                xCoords.add(vertex.getEntry(0));
                yCoords.add(vertex.getEntry(1));
                zCoords.add(vertex.getEntry(2));
            }
            PdVector mean = new PdVector(
                    EverythingHelper.mean(xCoords).doubleValue(),
                    EverythingHelper.mean(yCoords).doubleValue(),
                    EverythingHelper.mean(zCoords).doubleValue()
            );
            vertexOffsets[star.getCenterVertex()] = PdVector.subNew(mean, center);
        }

        for (int i = 0; i < m_geom.getVertices().length; i++) {
            PdVector scaledOffset = vertexOffsets[i];
            if (scaledOffset == null) continue;
            scaledOffset.multScalar(stepwidth);
            m_geom.setVertex(i, PdVector.addNew(m_geom.getVertex(i), scaledOffset));
        }

    }
}