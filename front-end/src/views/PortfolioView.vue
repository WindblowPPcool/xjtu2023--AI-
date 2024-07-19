<template>
 <div class="vanta-background" ref="vantaRef">
    <slot></slot>
  </div>
</template>

<script>
import * as THREE from "three";
import FOG from "vanta/src/vanta.fog";

export default {
  name: 'VantaBackground',
  data() {
    return {};
  },
  mounted() {
    this.vantaEffect = FOG({
      el: this.$refs.vantaRef,
      THREE: THREE,
    });
    VANTA.FOG({
      el: this.$refs.vantaRef,
      /*以下为样式配置*/
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 1000.0,
      minWidth: 200.0,
      highlightColor: 0xa577be,
      midtoneColor: 0xcd4841,
      lowlightColor: 0x473b6b,
      baseColor: 0xdfe3e1,
      blurFactor: 0.52,
      speed: 1.2,
      zoom: 0.3,
    });
  },
  beforeDestroy() {
    if (this.vantaEffect) {
      this.vantaEffect.destroy();
    }
  },
};
</script>

<style lang="scss" scoped>
.box {
  position: relative;
  .banner {
    z-index: 999;
    position: absolute;
    top: 30%;
    left: 10%;
    color: #fff;
  }
}
h1 {
  font-size: 66px;
}
p {
  margin-top: 60px;
  font-size: 18px;
}
</style>
