<template>
  <div class="nav" :class="store.getters.getDarkMode ? 'night-mode': 'darkcyan'">
    <ul>
      <li><router-link :to="{path: '/'}"><i class="fa fa-home"></i></router-link></li>
      <li><router-link :to="{path: '/gpt/chatGPT'}"><i class="fa fa-comments-o"></i><span>ChatGPT </span></router-link></li>
      <li class="nav-login" v-show="!isAvatarShow"><router-link :to="{path: '/user/login'}"><i class="fa fa-user"></i><span>登录</span></router-link></li>
      <li class="nav-login" v-show="isAvatarShow">
        <router-link :to="{path: '/'}">
          <i class="fa fa-user"></i>
          <el-avatar class="nav-login-avatar" shape="square" :style="`background:${extractColorByName(store.getters.getUser.name)}`">
            {{ $store.getters.getUser.name }}
          </el-avatar>
        </router-link>
        <ul class="sublevel menu">
          <li @click="toggleDark"><a>
            <el-switch
                class="nav-darkMode-switch"
                v-model="isDark"
                inline-prompt
                :active-icon="Sunny"
                :inactive-icon="Moon"
                style="--el-switch-on-color: #002B2E;"
            />
          </a></li>
          <li @click="logout"><a style="cursor: pointer">注销</a></li>
        </ul>
      </li>
      <li><router-link :to="{path: '/note/userNote'}"><i class="fa fa-book"></i><span>笔记</span></router-link></li>
      <li><a href="#"><i class="fa fa-link"></i><span>菜鸟教程 <i class="fa fa-chevron-down"></i></span></a>
        <div class="sublevel megamenu">
          <div class="holder-1">
            <ul>
              <li>
                <h3>HTML / CSS</h3>
              </li>
              <li><a title="HTML 教程" href="//www.runoob.com/html/html-tutorial.html" target="_blank">HTML</a></li>
              <li><a title="HTML5 教程" href="//www.runoob.com/html/html5-intro.html" target="_blank">HTML5</a></li>
              <li><a title="CSS 教程" href="//www.runoob.com/css/css-tutorial.html" target="_blank">CSS</a></li>
              <li><a title="CSS3 教程" href="//www.runoob.com/css3/css3-tutorial.html" target="_blank">CSS3</a></li>
              <li><a title="Bootstrap3 教程" href="//www.runoob.com/bootstrap/bootstrap-tutorial.html" target="_blank">Bootstrap3</a></li>
              <li><a title="Bootstrap4 教程" href="//www.runoob.com/bootstrap4/bootstrap4-tutorial.html" target="_blank">Bootstrap4</a></li>
              <li><a title="Bootstrap5 教程" href="//www.runoob.com/bootstrap5/bootstrap5-tutorial.html" target="_blank">Bootstrap5</a></li>
              <li><a title="Font Awesome 教程" href="//www.runoob.com/font-awesome/fontawesome-tutorial.html" target="_blank">Font Awesome</a></li>
              <li><a title="Foundation 教程" href="//www.runoob.com/foundation/foundation-tutorial.html" target="_blank">Foundation</a></li>
            </ul>
            <ul>
              <li>
                <h3>JavaScript</h3>
              </li>
              <li><a title="JavaScript 教程" href="//www.runoob.com/js/js-tutorial.html" target="_blank">JavaScript</a></li>
              <li><a title="HTML DOM 教程" href="//www.runoob.com/htmldom/htmldom-tutorial.html" target="_blank">HTML DOM</a></li>
              <li><a title="jQuery 教程" href="//www.runoob.com/jquery/jquery-tutorial.html" target="_blank">jQuery</a></li>
              <li><a title="AngularJS 教程" href="//www.runoob.com/angularjs/angularjs-tutorial.html" target="_blank">AngularJS</a></li>
              <li><a title="AngularJS2 教程" href="//www.runoob.com/angularjs2/angularjs2-tutorial.html" target="_blank">AngularJS2</a></li>
              <li><a title="Vue.js 教程" href="//www.runoob.com/vue2/vue-tutorial.html" target="_blank">Vue.js</a></li>
              <li><a title="Vue3 教程" href="//www.runoob.com/vue3/vue3-tutorial.html" target="_blank">Vue3</a></li>
              <li><a title="React 教程" href="//www.runoob.com/react/react-tutorial.html" target="_blank">React</a></li>
              <li><a title="TypeScript 教程" href="//www.runoob.com/typescript/ts-tutorial.html" target="_blank">TypeScript</a></li>
              <li><a title="jQuery UI 教程" href="//www.runoob.com/jqueryui/jqueryui-tutorial.html" target="_blank">jQuery UI</a></li>
              <li><a title="jQuery EasyUI 教程" href="//www.runoob.com/jeasyui/jqueryeasyui-tutorial.html" target="_blank">jQuery EasyUI</a></li>
              <li><a title="Node.js 教程" href="//www.runoob.com/nodejs/nodejs-tutorial.html" target="_blank">Node.js</a></li>
              <li><a title="AJAX 教程" href="//www.runoob.com/ajax/ajax-tutorial.html" target="_blank">AJAX</a></li>
              <li><a title="JSON 教程" href="//www.runoob.com/json/json-tutorial.html" target="_blank">JSON</a></li>
              <li><a title="Echarts 教程" href="//www.runoob.com/echarts/echarts-tutorial.html" target="_blank">Echarts</a></li>
              <li><a title="Chart.js 教程" href="//www.runoob.com/chartjs/chartjs-tutorial.html" target="_blank">Chart.js</a></li>
              <li><a title="Highcharts 教程" href="//www.runoob.com/highcharts/highcharts-tutorial.html" target="_blank">Highcharts</a></li>
              <li><a title="Google 地图 教程" href="//www.runoob.com/googleapi/google-maps-basic.html" target="_blank">Google</a></li>
            </ul>
            <ul>
              <li>
                <h3>服务端</h3>
              </li>
              <li><a title="Python 教程" href="//www.runoob.com/python3/python3-tutorial.html" target="_blank">Python</a></li>
              <li><a title="Python2.x 教程" href="//www.runoob.com/python/python-tutorial.html" target="_blank">Python2.x</a></li>
              <li><a title="Linux 教程" href="//www.runoob.com/linux/linux-tutorial.html" target="_blank">Linux</a></li>
              <li><a title="Docker 教程" href="//www.runoob.com/docker/docker-tutorial.html" target="_blank">Docker</a></li>
              <li><a title="Ruby 教程" href="//www.runoob.com/ruby/ruby-tutorial.html" target="_blank">Ruby</a></li>
              <li><a title="Java 教程" href="//www.runoob.com/java/java-tutorial.html" target="_blank">Java</a></li>
              <li><a title="C 教程" href="//www.runoob.com/c/c-tutorial.html" target="_blank">C</a></li>
              <li><a title="C++ 教程" href="//www.runoob.com/cplusplus/cpp-tutorial.html" target="_blank">C++</a></li>
              <li><a title="Perl 教程" href="//www.runoob.com/perl/perl-tutorial.html" target="_blank">Perl</a></li>
              <li><a title="Servlet 教程" href="//www.runoob.com/servlet/servlet-tutorial.html" target="_blank">Servlet</a></li>
              <li><a title="JSP 教程" href="//www.runoob.com/jsp/jsp-tutorial.html" target="_blank">JSP</a></li>
              <li><a title="Lua 教程" href="//www.runoob.com/lua/lua-tutorial.html" target="_blank">Lua</a></li>
              <li><a title="Rust 教程" href="//www.runoob.com/rust/rust-tutorial.html" target="_blank">Rust</a></li>
              <li><a title="Scala 教程" href="//www.runoob.com/scala/scala-tutorial.html" target="_blank">Scala</a></li>
              <li><a title="Go 教程" href="//www.runoob.com/go/go-tutorial.html" target="_blank">Go</a></li>
              <li><a title="PHP 教程" href="//www.runoob.com/php/php-tutorial.html" target="_blank">PHP</a></li>
              <li><a title="数据结构与算法" href="//www.runoob.com/data-structures/data-structures-tutorial.html" target="_blank">数据结构与算法</a></li>
              <li><a title="Django 教程" href="//www.runoob.com/django/django-tutorial.html" target="_blank">Django</a></li>
              <li><a title="Zookeeper 教程" href="//www.runoob.com/w3cnote/zookeeper-tutorial.html" target="_blank">Zookeeper</a></li>
              <li><a title="设计模式" href="//www.runoob.com/design-pattern/design-pattern-tutorial.html" target="_blank">设计模式</a></li>
              <li><a title="正则表达式" href="//www.runoob.com/regexp/regexp-tutorial.html" target="_blank">正则表达式</a></li>
              <li><a title="Maven 教程" href="//www.runoob.com/maven/maven-tutorial.html" target="_blank">Maven</a></li>
              <li><a title="Verilog 教程" href="//www.runoob.com/w3cnote/verilog-tutorial.html" target="_blank">Verilog</a></li>
              <li><a title="ASP 教程" href="//www.runoob.com/asp/asp-tutorial.html" target="_blank">ASP</a></li>
              <li><a title="AppML 教程" href="//www.runoob.com/appml/appml-tutorial.html" target="_blank">AppML</a></li>
              <li><a title="VBScript 教程" href="//www.runoob.com/vbscript/vbscript-tutorial.html" target="_blank">VBScript</a></li>
            </ul>
            <ul>
              <li>
                <h3>数据库</h3>
              </li>
              <li><a target="_blank" title="SQL 教程" href="//www.runoob.com/sql/sql-tutorial.html">SQL</a></li>
              <li><a target="_blank" title="MySQL 教程" href="//www.runoob.com/mysql/mysql-tutorial.html">MySQL</a></li>
              <li><a target="_blank" title="PostgreSQL 教程" href="//www.runoob.com/postgresql/postgresql-tutorial.html">PostgreSQL</a></li>
              <li><a target="_blank" title="SQLite 教程" href="//www.runoob.com/sqlite/sqlite-tutorial.html">SQLite</a></li>
              <li><a target="_blank" title="MongoDB 教程" href="//www.runoob.com/mongodb/mongodb-tutorial.html">MongoDB</a></li>
              <li><a target="_blank" title="Redis 教程" href="//www.runoob.com/redis/redis-tutorial.html">Redis</a></li>
              <li><a target="_blank" title="Memcached 教程" href="//www.runoob.com/Memcached/Memcached-tutorial.html">Memcached</a></li>
            </ul>
            <ul>
              <li>
                <h3>数据分析</h3>
              </li>
              <li><a target="_blank" title="Python 教程" href="//www.runoob.com/python3/python3-tutorial.html">Python</a></li>
              <li><a target="_blank" title="NumPy 教程" href="//www.runoob.com/numpy/numpy-tutorial.html">NumPy</a></li>
              <li><a target="_blank" title="Pandas 教程" href="//www.runoob.com/pandas/pandas-tutorial.html">Pandas</a></li>
              <li><a target="_blank" title="Matplotlib 教程" href="//www.runoob.com/matplotlib/matplotlib-tutorial.html">Matplotlib</a></li>
              <li><a target="_blank" title="Scipy 教程" href="//www.runoob.com/scipy/scipy-tutorial.html">Scipy</a></li>
              <li><a target="_blank" title="R 教程" href="//www.runoob.com/r/r-tutorial.html">R</a></li>
              <li><a target="_blank" title="Julia 教程" href="//www.runoob.com/julia/julia-tutorial.html">Julia</a></li>
            </ul>
            <ul>
              <li>
                <h3>移动端</h3>
              </li>
              <li><a target="_blank" title="Android 教程" href="//www.runoob.com/w3cnote/android-tutorial-intro.html">Android</a></li>
              <li><a target="_blank" title="Swift 教程" href="//www.runoob.com/swift/swift-tutorial.html">Swift</a></li>
              <li><a target="_blank" title="jQuery Mobile 教程" href="//www.runoob.com/jquerymobile/jquerymobile-tutorial.html">jQuery Mobile</a></li>
              <li><a target="_blank" title="ionic 教程" href="//www.runoob.com/ionic/ionic-tutorial.html">ionic</a></li>
              <li><a target="_blank" title="Kotlin 教程" href="//www.runoob.com/kotlin/kotlin-tutorial.html">Kotlin</a></li>
            </ul>
            <ul>
              <li>
                <h3>XML</h3>
              </li>
              <li><a target="_blank" title="XML 教程" href="//www.runoob.com/xml/xml-tutorial.html">XML</a></li>
              <li><a target="_blank" title="DTD 教程" href="//www.runoob.com/dtd/dtd-tutorial.html">DTD</a></li>
              <li><a target="_blank" title="XML DOM 教程" href="//www.runoob.com/dom/dom-tutorial.html">XML DOM</a></li>
              <li><a target="_blank" title="XSLT 教程" href="//www.runoob.com/xsl/xsl-tutorial.html">XSLT</a></li>
              <li><a target="_blank" title="XPath 教程" href="//www.runoob.com/xpath/xpath-tutorial.html">XPath</a></li>
              <li><a target="_blank" title="XQuery 教程" href="//www.runoob.com/xquery/xquery-tutorial.html">XQuery</a></li>
              <li><a target="_blank" title="XLink 教程" href="//www.runoob.com/xlink/xlink-tutorial.html">XLink</a></li>
              <li><a target="_blank" title="XPointer 教程" href="//www.runoob.com/xlink/xlink-tutorial.html">XPointer</a></li>
              <li><a target="_blank" title="XML Schema 教程" href="//www.runoob.com/schema/schema-tutorial.html">XML Schema</a></li>
              <li><a target="_blank" title="XSL-FO 教程" href="//www.runoob.com/xslfo/xslfo-tutorial.html">XSL-FO</a></li>
              <li><a target="_blank" title="SVG 教程" href="//www.runoob.com/svg/svg-tutorial.html">SVG</a></li>
            </ul>
            <ul>
              <li>
                <h3>ASP.NET</h3>
              </li>
              <li><a target="_blank" title="ASP.NET 教程" href="//www.runoob.com/aspnet/aspnet-tutorial.html">ASP.NET</a></li>
              <li><a target="_blank" title="C# 教程" href="//www.runoob.com/csharp/csharp-tutorial.html">C#</a></li>
              <li><a target="_blank" title="Web Pages 教程" href="//www.runoob.com/aspnet/webpages-intro.html">Web Pages</a></li>
              <li><a target="_blank" title="Razor 教程" href="//www.runoob.com/aspnet/razor-intro.html">Razor</a></li>
              <li><a target="_blank" title="MVC 教程" href="//www.runoob.com/aspnet/mvc-intro.html">MVC</a></li>
              <li><a target="_blank" title="Web Forms 教程" href="//www.runoob.com/aspnet/aspnet-intro.html">Web Forms</a></li>
            </ul>
            <ul>
              <li>
                <h3>Web Service</h3>
              </li>
              <li><a target="_blank" title="Web Service 教程" href="//www.runoob.com/webservices/webservices-tutorial.html">Web Service</a></li>
              <li><a target="_blank" title="WSDL 教程" href="//www.runoob.com/wsdl/wsdl-tutorial.html">WSDL</a></li>
              <li><a target="_blank" title="SOAP 教程" href="//www.runoob.com/soap/soap-tutorial.html">SOAP</a></li>
              <li><a target="_blank" title="RSS 教程" href="//www.runoob.com/rss/rss-tutorial.html">RSS</a></li>
              <li><a target="_blank" title="RDF 教程" href="//www.runoob.com/rdf/rdf-tutorial.html">RDF</a></li>
            </ul>
            <ul>
              <li>
                <h3>开发工具</h3>
              </li>
              <li><a target="_blank" title="Eclipse 教程" href="//www.runoob.com/eclipse/eclipse-tutorial.html">Eclipse</a></li>
              <li><a target="_blank" title="Git 教程" href="//www.runoob.com/git/git-tutorial.html">Git</a></li>
              <li><a target="_blank" title="Svn 教程" href="//www.runoob.com/svn/svn-tutorial.html">Svn</a></li>
              <li><a target="_blank" title="Markdown 教程" href="//www.runoob.com/markdown/md-tutorial.html">Markdown</a></li>
            </ul>
            <ul>
              <li>
                <h3>网站建设</h3>
              </li>
              <li><a target="_blank" title="HTTP 教程" href="//www.runoob.com/http/http-tutorial.html">HTTP</a></li>
              <li><a target="_blank" title="网站建设指南" href="//www.runoob.com/web/web-buildingprimer.html">网站建设指南</a></li>
              <li><a target="_blank" title="浏览器信息" href="//www.runoob.com/browsers/browser-information.html">浏览器信息</a></li>
              <li><a target="_blank" title="网站主机教程" href="//www.runoob.com/hosting/hosting-tutorial.html">网站主机教程</a></li>
              <li><a target="_blank" title="TCP/IP 教程" href="//www.runoob.com/tcpip/tcpip-tutorial.html">TCP/IP</a></li>
              <li><a target="_blank" title="W3C 教程" href="//www.runoob.com/w3c/w3c-tutorial.html">W3C</a></li>
              <li><a target="_blank" title="网站品质" href="//www.runoob.com/quality/quality-tutorial.html">网站品质</a></li>
            </ul>
          </div>
        </div>
      </li>
    </ul>
  </div>
  <br>
  <br>
</template>

<!-- setup 是 vue3 的新特性 -->
<script setup>
import { useDark, useToggle } from '@vueuse/core';
import { onMounted, ref } from 'vue';
import { Sunny, Moon } from '@element-plus/icons-vue';
import store from "@/store";
import router from "@/router";

const isAvatarShow = ref(false);
const isDark = useDark();

// 注销
const logout = () => {
  store.dispatch('asyncClearUser');
  localStorage.clear();
  isAvatarShow.value = false;
  router.push('/');
};

// 切换夜间模式
const toggleDark = () => {
  useToggle(isDark);
  store.commit("updateDarkMode",isDark);
};

// 根据头像名字切换头像颜色
const extractColorByName = (name) => {
  var temp = [];
  temp.push("#");
  for (let index = 0; index < name.length; index++) {
    temp.push(parseInt(name[index].charCodeAt(0), 10).toString(24));
  }
  return temp.slice(0, 5).join('').slice(0, 4);
}

onMounted(() => {
  if (store.getters.getUser.phone) {
    isAvatarShow.value = true;
  }
});
</script>

<style scoped>
</style>
