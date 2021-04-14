<template>
  <div style="width: 100%;padding: 24px;min-width: 1600px;">
    <div style="margin-bottom:16px;">
      <a-row type="flex" justify="space-between" :gutter="16">
        <a-col :span="6">
          <div style="height: 100px;background: #ffffff;">
            <a-row type="flex" justify="space-between">
              <a-col :span="9">
                <div style="height: 100px;background: #fff;text-align: center">
                  <div style="padding-top:20px;font-size: 30px;color: #0f70f0;letter-spacing: -2px;font-weight: bold;">
                    {{ this.pv }}
                  </div>
                  <div style="font-size: 14px;">
                    点击量PV
                  </div>
                </div>
              </a-col>
              <a-col :span="14">
                <div id="pvChart" style="margin:10px 0;height: 80px;background: #fff;">
                  图表
                </div>
              </a-col>
            </a-row>
          </div>
        </a-col>
        <a-col :span="6">
          <div style="height: 100px;background: #fff;">
            <a-row type="flex" justify="space-between">
              <a-col :span="9">
                <div style="height: 100px;background: #fff;text-align: center">
                  <div style="padding-top:20px;font-size: 30px;color: #0f70f0;letter-spacing: -2px;font-weight: bold;">
                    {{ this.uv }}
                  </div>
                  <div style="font-size: 14px;">
                    用户量UV
                  </div>
                </div>
              </a-col>
              <a-col :span="14">
                <div id="uvChart" style="margin:10px 0;height: 80px;background: #fff;">
                  图表
                </div>
              </a-col>
            </a-row>
          </div>
        </a-col>
        <a-col :span="6">
          <div style="height: 100px;background: #fff;">
            <a-row type="flex" justify="space-between">
              <a-col :span="9">
                <div style="height: 100px;background: #fff;text-align: center">
                  <div style="padding-top:20px;font-size: 30px;color: #0f70f0;letter-spacing: -2px;font-weight: bold;">
                    {{ this.df }}
                  </div>
                  <div style="font-size: 14px;">
                    日文件数量
                  </div>
                </div>
              </a-col>
              <a-col :span="14">
                <div id="dfChart" style="margin:10px 0;height: 80px;background: #fff;">
                  图表
                </div>
              </a-col>
            </a-row>
          </div>
        </a-col>
        <a-col :span="6">
          <div style="height: 100px;background: #fff;">
            <a-row type="flex" justify="space-between">
              <a-col :span="9">
                <div style="height: 100px;background: #fff;text-align: center">
                  <div style="padding-top:20px;font-size: 30px;color: #0f70f0;letter-spacing: -2px;font-weight: bold;">
                    {{ this.ds }}
                  </div>
                  <div style="font-size: 14px;">
                    日分享数量
                  </div>
                </div>
              </a-col>
              <a-col :span="14">
                <div id="dsChart" style="margin:10px 0;height: 80px;background: #fff;">
                  图表
                </div>
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-row>
    </div>
    <div style="margin-bottom:16px;">
      <a-row type="flex" justify="space-between" :gutter="16">
        <a-col :span="18">
          <div id="monthChart" style="padding-top:20px;height: 580px;background: #fff;">
            本周操作图
          </div>
        </a-col>
        <a-col :span="6">
          <div id="fileChart" style="padding-top:20px;height: 300px;background: #fff;">
            文件类型占比与总文件量
          </div>
          <div id="downloadChart" style="padding-top:20px;height: 300px;background: #fff;">
            下载类型占比与文件总量
          </div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
import * as api from "../../api/controller";
import {message} from "../../util/message";
import {getFormatSize} from "../../util/util";


export default {
  name: 'Controller',
  data() {
    return {
      pv: 0,
      uv: 0,
      df: 0,
      ds: 0,
      file: 0,
      download: 0,
      pvData: {
        name: ['2021年4月13日', '2021年4月14日', '2021年4月15日', '2021年4月16日', '2021年4月17日', '2021年4月18日', '2021年4月19日'],
        list: [150, 230, 224, 218, 135, 147, 260]
      },
      uvData: {
        name: ['2021年4月13日', '2021年4月14日', '2021年4月15日', '2021年4月16日', '2021年4月17日', '2021年4月18日', '2021年4月19日'],
        list: [150, 230, 224, 218, 135, 147, 260]
      },
      dfData: {
        name: ['2021年4月13日', '2021年4月14日', '2021年4月15日', '2021年4月16日', '2021年4月17日', '2021年4月18日', '2021年4月19日'],
        list: [150, 230, 224, 218, 135, 147, 260]
      },
      dsData: {
        name: ['2021年4月13日', '2021年4月14日', '2021年4月15日', '2021年4月16日', '2021年4月17日', '2021年4月18日', '2021年4月19日'],
        list: [150, 230, 224, 218, 135, 147, 260]
      },
      monthData: {
        legend: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎'],
        name: ['2021年4月13日', '2021年4月14日', '2021年4月15日', '2021年4月16日', '2021年4月17日', '2021年4月18日', '2021年4月19日'],
        list: [
          {
            name: '邮件营销',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '联盟广告',
            type: 'line',
            stack: '总量',
            data: [220, 182, 191, 234, 290, 330, 310]
          },
          {
            name: '视频广告',
            type: 'line',
            stack: '总量',
            data: [150, 232, 201, 154, 190, 330, 410]
          },
          {
            name: '直接访问',
            type: 'line',
            stack: '总量',
            data: [320, 332, 301, 334, 390, 330, 320]
          },
          {
            name: '搜索引擎',
            type: 'line',
            stack: '总量',
            data: [820, 932, 901, 934, 1290, 1330, 1320]
          }
        ]
      },
      fileData: {
        list: [
          {value: 1048, name: '搜索引擎'},
          {value: 735, name: '直接访问'},
          {value: 580, name: '邮件营销'},
          {value: 484, name: '联盟广告'},
          {value: 300, name: '视频广告'}
        ]
      },
      downloadData: {
        list: [
          {value: 1048, name: '搜索引擎'},
          {value: 735, name: '直接访问'},
          {value: 580, name: '邮件营销'},
          {value: 484, name: '联盟广告'},
          {value: 300, name: '视频广告'}
        ]
      },
      totalFileSize: "总文件量100TB",
    };
  },
  mounted() {
    setTimeout(() => {
      this.init();
    }, 20)
  },
  methods: {
    init() {
      this.getPvData();
      this.getUvData();
      this.getDfData();
      this.getDsData();
      this.getMonthData();
      this.getFileData();
      this.getDownloadData();
    },
    getPvData() {
      let parent = this;
      const handler = (data) => {
        parent.pvData = data;
        parent.pv = data.list[6];
        parent.pvChart(parent.pvData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getPvData(handler, catcher);
    },
    getUvData() {
      let parent = this;
      const handler = (data) => {
        parent.uvData = data;
        parent.uv = data.list[6];
        parent.uvChart(parent.uvData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getUvData(handler, catcher);
    },
    getDfData() {
      let parent = this;
      const handler = (data) => {
        parent.dfData = data;
        parent.df = data.list[6];
        parent.dfChart(parent.dfData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getDfData(handler, catcher);
    },
    getDsData() {
      let parent = this;
      const handler = (data) => {
        parent.dsData = data;
        parent.ds = data.list[6];
        parent.dsChart(parent.dsData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getDsData(handler, catcher);
    },
    getMonthData() {
      let parent = this;
      const handler = (data) => {
        parent.monthData = data;
        parent.monthChart(parent.monthData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getMonthData(handler, catcher);
    },
    getFileData() {
      let parent = this;
      const handler = (data) => {
        parent.fileData = data;
        parent.totalFileSize = "总文件量" + getFormatSize(data.total, 0);
        parent.fileChart(parent.fileData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getFileData(handler, catcher);
    },
    getDownloadData() {
      let parent = this;
      const handler = (data) => {
        parent.downloadData = data;
        parent.totalFileSize = "总下载量" + getFormatSize(data.total, 0);
        parent.downloadChart(parent.downloadData);
      };
      const catcher = (code, content) => {
        message(content, "warning")
      };
      api.getDownloadData(handler, catcher);
    },
    pvChart(data) {
      this.$echarts.dispose(document.getElementById('pvChart'));
      var myChart = this.$echarts.init(document.getElementById('pvChart'));
      var option = {
        title: {
          show: false
        },
        grid: {
          top: '2',
          left: '0',
          right: '0',
          bottom: '2',
          show: false
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
        },
        legend: {
          padding: 0
        },
        xAxis: {
          type: 'category',
          data: data.name,
          show: false
        },
        yAxis: {
          type: 'value',
          scale: true,
          show: false
        },
        series: [{
          data: data.list,
          type: 'line',
          symbol: 'none',
          smooth: false
        }]
      };
      myChart.setOption(option);
    },
    uvChart(data) {
      this.$echarts.dispose(document.getElementById('uvChart'));
      var myChart = this.$echarts.init(document.getElementById('uvChart'));
      var option = {
        title: {
          show: false
        },
        grid: {
          top: '2',
          left: '0',
          right: '0',
          bottom: '2',
          show: false
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
        },
        legend: {
          padding: 0
        },
        xAxis: {
          type: 'category',
          data: data.name,
          show: false
        },
        yAxis: {
          type: 'value',
          scale: true,
          show: false
        },
        series: [{
          data: data.list,
          type: 'line',
          symbol: 'none',
          smooth: false
        }]
      };
      myChart.setOption(option);
    },
    dfChart(data) {
      this.$echarts.dispose(document.getElementById('dfChart'));
      var myChart = this.$echarts.init(document.getElementById('dfChart'));
      var option = {
        title: {
          show: false
        },
        grid: {
          top: '2',
          left: '0',
          right: '0',
          bottom: '2',
          show: false
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
        },
        legend: {
          padding: 0
        },
        xAxis: {
          type: 'category',
          data: data.name,
          show: false
        },
        yAxis: {
          type: 'value',
          scale: true,
          show: false
        },
        series: [{
          data: data.list,
          type: 'line',
          symbol: 'none',
          smooth: false
        }]
      };
      myChart.setOption(option);
    },
    dsChart(data) {
      this.$echarts.dispose(document.getElementById('dsChart'));
      var myChart = this.$echarts.init(document.getElementById('dsChart'));
      var option = {
        title: {
          show: false
        },
        grid: {
          top: '2',
          left: '0',
          right: '0',
          bottom: '2',
          show: false
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            animation: false
          }
        },
        legend: {
          padding: 0
        },
        xAxis: {
          type: 'category',
          data: data.name,
          show: false
        },
        yAxis: {
          type: 'value',
          scale: true,
          show: false
        },
        series: [{
          data: data.list,
          type: 'line',
          symbol: 'none',
          smooth: false
        }]
      };
      myChart.setOption(option);
    },
    monthChart(data) {
      this.$echarts.dispose(document.getElementById('monthChart'));
      var myChart = this.$echarts.init(document.getElementById('monthChart'));
      var option = {
        title: {
          text: '本周操作图',
          left: 30,
          textStyle: {
            fontSize: 20
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: data.legend
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.name
        },
        yAxis: {
          type: 'value'
        },
        series: data.list
      };
      myChart.setOption(option);
    },
    fileChart(data) {
      this.$echarts.dispose(document.getElementById('fileChart'));
      var myChart = this.$echarts.init(document.getElementById('fileChart'));
      var option = {
        title: {
          text: '文件类型统计',
          subtext: this.totalFileSize,
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '50%',
            data: data.list,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      myChart.setOption(option);
    },
    downloadChart(data) {
      this.$echarts.dispose(document.getElementById('downloadChart'));
      var myChart = this.$echarts.init(document.getElementById('downloadChart'));
      var option = {
        title: {
          text: '下载类型统计',
          subtext: this.totalFileSize,
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '50%',
            data: data.list,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      myChart.setOption(option);
    },
  }
};
</script>
