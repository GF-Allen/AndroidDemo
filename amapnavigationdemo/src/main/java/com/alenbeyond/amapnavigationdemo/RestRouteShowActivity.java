package com.alenbeyond.amapnavigationdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestRouteShowActivity extends Activity implements AMapNaviListener, OnClickListener, Inputtips.InputtipsListener {
    private boolean congestion, cost, hightspeed, avoidhightspeed;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    private AMap mAmap;
    /**
     * 地图对象
     */
    private MapView mRouteMapView;
    private Marker mStartMarker;
    private Marker mEndMarker;
    /**
     * 选择起点Action标志位
     */
    private boolean mapClickStartReady;
    /**
     * 选择终点Aciton标志位
     */
    private boolean mapClickEndReady;
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    /**
     * 当前用户选中的路线，在下个页面进行导航
     */
    private int routeIndex;
    /**路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线**/
    private int zindex = 1;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;
    private boolean chooseRouteSuccess = false;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    private ListView lvResult;
    private SimpleAdapter aAdapter;
    private EditText etEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_calculate);
        Button gpsnavi = (Button) findViewById(R.id.gpsnavi);
        Button emulatornavi = (Button) findViewById(R.id.emulatornavi);

        etEnd = (EditText) findViewById(R.id.et_end);
        Button btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(this);
        gpsnavi.setOnClickListener(this);
        emulatornavi.setOnClickListener(this);

        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);

        //定位监听
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LatLng startPoint = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                mAmap.moveCamera(CameraUpdateFactory.changeLatLng(startPoint));
                mStartMarker.setPosition(startPoint);
                startList.add(new NaviLatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude()));
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        mRouteMapView = (MapView) findViewById(R.id.navi_view);
        mRouteMapView.onCreate(savedInstanceState);
        mAmap = mRouteMapView.getMap();
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(16));

        //添加标注点击监听
        AMap.OnMarkerClickListener listener = new AMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                return false;
            }
        };
        mAmap.setOnMarkerClickListener(listener);
        mAmap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.setTitle(marker.getTitle());
            }
        });
        mAmap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View infoContent = getLayoutInflater().inflate(
                        R.layout.custom_info_contents, null);
                render(marker, infoContent);
                return infoContent;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(
                        R.layout.custom_info_contents, null);

                render(marker, infoWindow);
                return infoWindow;
            }

            /**
             * 自定义infowinfow窗口
             */
            public void render(final Marker marker, View view) {

                String title = marker.getTitle();
                TextView titleUi = ((TextView) view.findViewById(R.id.title));
                if (title != null) {
                    SpannableString titleText = new SpannableString(title);
                    titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
                            titleText.length(), 0);
                    titleUi.setTextSize(15);
                    titleUi.setText(titleText);

                } else {
                    titleUi.setText("");
                }
                String snippet = marker.getSnippet();
                TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));

                TextView tvOk = (TextView) view.findViewById(R.id.btn_ok);
                tvOk.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endList.clear();
                        endList.add(new NaviLatLng(marker.getPosition().latitude,marker.getPosition().longitude));
                        int strategyFlag = 0;
                        try {
                            strategyFlag = mAMapNavi.strategyConvert(true, false, false, false, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (strategyFlag >= 0) {
                            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                        }
                    }
                });
                if (snippet != null) {
                    SpannableString snippetText = new SpannableString(snippet);
                    snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                            snippetText.length(), 0);
                    snippetUi.setTextSize(20);
                    snippetUi.setText(snippetText);
                } else {
                    snippetUi.setText("");
                }
            }
        });
        // 初始化Marker添加到地图
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.start))));
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.end))));

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mRouteMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRouteMapView.onPause();
        startList.clear();
        wayList.clear();
        endList.clear();
        routeOverlays.clear();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRouteMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteMapView.onDestroy();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
    }


    @Override
    public void onInitNaviSuccess() {
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }

    @Override
    public void onCalculateRouteSuccess() {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        AMapNaviPath path = mAMapNavi.getNaviPath();

        Intent intent = new Intent(getApplicationContext(), RouteNaviActivity.class);
        intent.putExtra("gps", false);
        startActivity(intent);
        this.finish();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */
//        drawRoutes(-1, path);
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        calculateSuccess = false;
        Toast.makeText(getApplicationContext(), "计算路线失败，errorcode＝" + arg0, Toast.LENGTH_SHORT).show();
    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
        routeOverLay.setTrafficLine(true);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }

    public void changeRoute() {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            chooseRouteSuccess = true;
            Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPath()).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPath()).getAllTime() + "s", Toast.LENGTH_SHORT).show();
            return;
        }

        if (routeIndex >= routeOverlays.size())
            routeIndex = 0;
        int routeID = routeOverlays.keyAt(routeIndex);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.7f);
        }
        routeOverlays.get(routeID).setTransparency(0);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);

        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
        Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPaths()).get(routeID).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPaths()).get(routeID).getAllTime() + "s", Toast.LENGTH_SHORT).show();
        routeIndex++;

        chooseRouteSuccess = true;
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.calculate:
//                clearRoute();
//                mapClickStartReady = false;
//                mapClickEndReady = false;
//                if (avoidhightspeed && hightspeed) {
//                    Toast.makeText(getApplicationContext(), "不走高速与高速优先不能同时为true.", Toast.LENGTH_LONG).show();
//                }
//                if (cost && hightspeed) {
//                    Toast.makeText(getApplicationContext(), "高速优先与避免收费不能同时为true.", Toast.LENGTH_LONG).show();
//                }
//            /*
//			 * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
//			 * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
//			 */
//                int strategyFlag = 0;
//                try {
//                    strategyFlag = mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (strategyFlag >= 0) {
//                    mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
//                    Toast.makeText(getApplicationContext(), "策略:" + strategyFlag, Toast.LENGTH_LONG).show();
//                }
//                break;
//            case R.id.startpoint:
//                Toast.makeText(this, "请在地图上点选起点", Toast.LENGTH_SHORT).show();
//                mapClickStartReady = true;
//                break;
//            case R.id.endpoint:
//                Toast.makeText(this, "请在地图上点选终点", Toast.LENGTH_SHORT).show();
//                mapClickEndReady = true;
//                break;
//            case R.id.selectroute:
//                changeRoute();
//                break;
            case R.id.gpsnavi:
                Intent gpsintent = new Intent(getApplicationContext(), RouteNaviActivity.class);
                gpsintent.putExtra("gps", true);
                startActivity(gpsintent);
                this.finish();
                break;
            case R.id.btn_start:
                //搜索提示
                InputtipsQuery inputquery = new InputtipsQuery(etEnd.getText().toString(), "");
                inputquery.setCityLimit(true);
                Inputtips inputTips = new Inputtips(RestRouteShowActivity.this, inputquery);
                inputTips.setInputtipsListener(RestRouteShowActivity.this);
                inputTips.requestInputtipsAsyn();
                break;
            case R.id.emulatornavi:

                int strategyFlag = 0;
                try {
                    strategyFlag = mAMapNavi.strategyConvert(true, false, false, false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strategyFlag >= 0) {
                    mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                }
                break;
            default:
                break;
        }
    }

    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {


    }

    @Override
    public void hideCross() {


    }

    @Override
    public void hideLaneInfo() {


    }

    @Override
    public void notifyParallelRoad(int arg0) {


    }

    @Override
    public void onArriveDestination() {


    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArrivedWayPoint(int arg0) {


    }

    @Override
    public void onEndEmulatorNavi() {


    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {


    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {


    }

    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {


    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {


    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {


    }

    @Override
    public void onReCalculateRouteForTrafficJam() {


    }

    @Override
    public void onReCalculateRouteForYaw() {


    }

    @Override
    public void onStartNavi(int arg0) {


    }

    @Override
    public void onTrafficStatusUpdate() {


    }

    @Override
    public void showCross(AMapNaviCross arg0) {


    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {


    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {


    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat arg0) {


    }

    /**
     * 搜索提示
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {
            for (int i = 0; i < tipList.size(); i++) {
                Tip tip = tipList.get(i);
                LatLng latLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                if (i == 0) {
                    mAmap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                }
                mAmap.addMarker(new MarkerOptions().
                        position(latLng).
                        title(tip.getName()).
                        snippet(tip.getAddress()));
            }
        } else {
            Toast.makeText(RestRouteShowActivity.this, "查找失败", Toast.LENGTH_SHORT).show();
        }
    }
}
