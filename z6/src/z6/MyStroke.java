package z6;

import java.awt.*;
import java.awt.geom.*;

public class MyStroke implements Stroke{
    private float ampl;
    private float elLength;
    private BasicStroke bs = new BasicStroke();

    public MyStroke(float ampl, float elLength) {
        this.ampl = ampl;
        this.elLength = elLength;
    }
    
    public Shape createStrokedShape(Shape plot) {
        GeneralPath res = new GeneralPath();
        PathIterator it = new FlatteningPathIterator(plot.getPathIterator(null), 1);
        float[] points = new float[2];
        float next = 0, mvX = 0, mvY = 0, prevX = 0, prevY = 0, curX = 0, curY = 0;
        int phase = 0, type = 0;

        while( !it.isDone()) {
            type = it.currentSegment( points );
            switch( type ){
                case PathIterator.SEG_MOVETO:
                    mvX = prevX = points[0];
                    mvY = prevY = points[1];
                    res.moveTo( mvX, mvY );
                    next = elLength ;
                    break;

                case PathIterator.SEG_CLOSE:
                    points[0] = mvX;
                    points[1] = mvY;
                case PathIterator.SEG_LINETO:
                    curX = points[0];
                    curY = points[1];
                    float dx = curX - prevX;
                    float dy = curY - prevY;

                    float distance = (float)Math.sqrt( dx * dx + dy * dy ) / 1.7f;
                    if ( distance >= next ) {
                        float r = 1.0f/distance;
                        while ( distance >= next ) {
                        	
                            float x = prevX + next * r * dx;
                            float y = prevY + next * r * dy;
                            if(phase %2 == 0)
                            	res.quadTo(x + ampl, y - 10, x + ampl / 2, y);
                            else
                            	res.lineTo(x + ampl, y );
                            next += elLength;
                            phase++;
                        }
                    }
                    next -= distance;
                    prevX = curX;
                    prevY = curY;
                    if ( type == PathIterator.SEG_CLOSE ) {
                        System.out.println("SegClose");
                        res.closePath();
                    }
                    break;
            }
            it.next();
        }
        return bs.createStrokedShape(res);
    }
}
