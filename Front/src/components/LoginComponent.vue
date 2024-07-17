<script>
import { login } from '@/services/AuthService'; // Ajusta la ruta según tu configuración

export default {
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await login(this.username, this.password);
        console.log('Login successful', response);

        // Redirige a la página de inicio o a otra página segura
        this.$router.push({ name: 'ProductList' });

      } catch (error) {
        console.error('Login failed', error);
        alert('Login failed: ' + (error.response?.data?.message || 'Invalid credentials'));
      }
    }
  }
};
</script>

<template>
  <section class="vh-100" style="background-image: url('https://mdbcdn.b-cdn.net/img/Photos/new-templates/search-box/img4.webp');">
    <div class="mask d-flex align-items-center h-100 gradient-custom-3">
    <div class="container py-5 h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col col-xl-10">
          <div class="card" style="border-radius: 1rem;">
            <div class="row g-0">
              <div class="col-md-6 col-lg-5 d-none d-md-block">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/img1.webp"
                  alt="login form" class="img-fluid" style="border-radius: 1rem 0 0 1rem;" />
              </div>
              <div class="col-md-6 col-lg-7 d-flex align-items-center">
                <div class="card-body p-4 p-lg-5 text-black">
                  <form @submit.prevent="login">

                    <div class="d-flex align-items-center mb-3 pb-1">
                      <i class="fas fa-cubes fa-2x me-3" style="color: #ff6219;"></i>
                      <span class="h1 fw-bold mb-0">Login</span>
                    </div>

                    <h5 class="fw-normal mb-3 pb-3" >Sign into your account</h5>

                    <div class="form mb-4">
                      <label class="form-label" for="form2Example17">Username</label>
                      <input v-model="username" type="email" id="form2Example17" class="form-control form-control-lg" />
                      
                    </div>

                    <div class="form mb-4">
                      <label class="form-label" for="form2Example27">Password</label>
                      <input v-model="password" type="password" id="form2Example27" class="form-control form-control-lg" />
                    </div>

                    <div class="pt-1 mb-4">
                      <button class="btn btn-dark btn-lg btn-block" type="submit">Login</button>
                    </div>
                    <p class="mb-5 pb-lg-2" style="color: #393f81;">Don't have an account? <router-link to="/register" class="mb-5 pb-lg-2" style="color: #393f81;">Register here</router-link></p>
                       

                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </section>
</template>



<style>
.gradient-custom-3 {
  /* fallback for old browsers */
  background: #84fab0;

  /* Chrome 10-25, Safari 5.1-6 */
  background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));

  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5))
}</style>