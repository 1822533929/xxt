<template>
  <div>
    <div id="container" ref="amap"></div>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader';

export default {
  name: "MapView",
  props: {
    longitude: {
      type: Number,
      required: true
    },
    latitude: {
      type: Number,
      required: true
    },
    location: {
      type: String,
      required: true
    }
  },

  mounted() {
    this.initAMap();
  },

  methods: {
    initAMap() {
      AMapLoader.load({
        key: 'f0e2c06fa9d28fa371bb2b1dc4ae4efc',
        version: '2.0',
        plugins: ['AMap.Marker']
      })
      .then((AMap) => {
        const map = new AMap.Map('container', {
          viewMode: '3D',
          zoom: 15,
          zooms: [5, 30],
          center: [this.longitude, this.latitude]
        });

        // 添加标记点
        const marker = new AMap.Marker({
          position: [this.longitude, this.latitude],
          title: this.location
        });
        map.add(marker);

        // 添加控件
        AMap.plugin(
          [
            'AMap.ElasticMarker',
            'AMap.Scale',
            'AMap.ToolBar',
            'AMap.HawkEye',
            'AMap.MapType',
            'AMap.Geolocation',
          ],
          () => {
            map.addControl(new AMap.ElasticMarker());
            map.addControl(new AMap.Scale());
            map.addControl(new AMap.ToolBar());
            map.addControl(new AMap.HawkEye());
            map.addControl(new AMap.MapType());
            map.addControl(new AMap.Geolocation());
          }
        );
        this.map = map;
      })
      .catch((e) => {
        console.error(e);
      });
    },
  },

  watch: {
    longitude(newVal) {
      if (this.map) {
        this.map.setCenter([newVal, this.latitude]);
      }
    },
    latitude(newVal) {
      if (this.map) {
        this.map.setCenter([this.longitude, newVal]);
      }
    }
  }
}
</script>

<style scoped>
#container {
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 500px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}
</style>
