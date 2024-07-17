<template>
  <section class="vh-100 bg-image"
    style="background-image: url('https://mdbcdn.b-cdn.net/img/Photos/new-templates/search-box/img4.webp');">
    <div class="mask d-flex align-items-center h-100 gradient-custom-3">
      <div class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col-12 col-md-9 col-lg-7 col-xl-6">
            <div class="card" style="border-radius: 15px;">
              <div class="card-body p-5">
                <h2 class="text-uppercase text-center mb-5">Create an account</h2>

                <form @submit.prevent="register">
                  <div class="form-group">
    <label for="username">Username</label>
    <input type="text" id="username" class="form-control" v-model="username" required>
  </div>

  <div class="form-group">
    <label for="password">Password</label>
    <input type="password" id="password" class="form-control" v-model="password" required>
  </div>

  <div class="form-group">
    <label for="firstname">First Name</label>
    <input type="text" id="firstname" class="form-control" v-model="firstname" required>
  </div>

  <div class="form-group">
    <label for="lastname">Last Name</label>
    <input type="text" id="lastname" class="form-control" v-model="lastname" required>
  </div>
  
  <div class="form-group">
    <label for="country">Country</label>
    <input type="text" id="country" class="form-control" v-model="country" required>
  </div>

                  <div class="d-flex justify-content-center">
                    <button type="submit"
                      class="btn btn-success btn-block btn-lg gradient-custom-4 text-body">Register</button>
                  </div>

                  <p class="text-center text-muted mt-5 mb-0">Already have an account? <router-link to="/"
                      style="color: #393f81;">Login here</router-link></p>
                </form>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { register } from '@/services/AuthService';

export default {
  data() {
    return {
      username: '',
      password: '',
      firstname: '',
      lastname: '',
      country: ''
    };
  },

  methods: {
    async register() {
      try {
        const userData = {
          username: this.username,
          password: this.password,
          firstname: this.firstname,
          lastname: this.lastname,
          country: this.country
        };

        const response = await register(userData);
        console.log('Registered successfully:', response);
        this.$router.push({ name: 'ProductList' });
      } catch (error) {
        console.error('Registration failed:', error);
        alert('Registration failed: ' + (error.response?.data?.message || 'Unknown error'));
      }
    }
  }
};
</script>

<style scoped>
.gradient-custom-3 {
  background: #84fab0;
  background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
  background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5))
}

.gradient-custom-4 {
  background: #84fab0;
  background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
  background: linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1))
}
</style>