/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.common.Constant
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:06
 * Email: huajianmao@gmail.com
 *************************************************************/
package avatar.core.utils.common;

import java.text.DecimalFormat;

public class Constant {

    // FIXME:
    public static final String RegMapDir = "/tmp/data/maps/region";

    public static DecimalFormat gpsDec = new DecimalFormat("0.000000");
    public static final City defaultCity = City.cities.get("shanghai");
    public static OrientType DEFAULT_ORIENT_TYPE = OrientType.Right;
    public static final int DEFAULT_ROAD_WIDTH = 10;
    public static double CELL_UNIT_LENGTH = 1000.0;
    public static final int VER_EDGE_ID_OFFSET = 100000;


    // Map matching related constants
    public static int DEFAULT_ROAD_ID = 1;
    public static final double NORMAL_DISTANCE_ERROR_MEAN = 0;
    public static final double NORMAL_DISTANCE_ERROR_DEVIATION = 20;

    public static final double MAX_DISTANCE_ERROR = 175;

    public static final int MAX_CAND_NUM = 10;
    public static final int DEFAULT_QUEUE_SIZE = MAX_CAND_NUM / 2;


    public static final short MAX_SPEED = 3000; //150; // km/h
    public static final double MIN_DISTANCE = 1e-1;
    public static final int MAX_WIDTH = 10;
    public static final double MIN_LNG_DELTA = 1e-8;
    public static final double MIN_REPORT_DISTANCE = 0.1;
    public static final double MAX_TWO_POINT_DISTANCE_SHIFT = 0.01; // 0.01 meters
    public static final double MAX_TWO_DISTANCE_SHIFT_PERCENT = 0.00001;


    public enum OrientType {
        Left, Right,
    }
    public enum RouteType {
        AStar, Dijkstra, Guide,
    }
}
