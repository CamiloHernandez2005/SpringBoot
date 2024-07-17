import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from './axios-interceptor';

import 'primevue/resources/themes/saga-blue/theme.css'; // Tema de PrimeVue
import 'primevue/resources/primevue.min.css'; // Estilos de PrimeVue
import 'primeicons/primeicons.css'; // Iconos de PrimeIcons
import 'bootstrap/dist/css/bootstrap.min.css'; // Estilos Bootstrap CSS
import 'mdb-ui-kit/css/mdb.min.css'; // Estilos MDB UI Kit CSS

import 'bootstrap'; // Bootstrap JavaScript
import 'bootstrap/dist/js/bootstrap.bundle.min.js'; // Scripts Bootstrap JS
import { initMDB } from 'mdb-ui-kit'; // Importa función de inicialización de MDB UI Kit
import { Dropdown, Collapse } from 'mdb-ui-kit'; // Importa Dropdown y Collapse de MDB UI Kit

// Inicializa componentes específicos de MDB UI Kit que vayas a utilizar
initMDB({ Dropdown, Collapse });

const app = createApp(App);
app.use(router);
app.config.globalProperties.$http = axios;
app.mount('#app');
