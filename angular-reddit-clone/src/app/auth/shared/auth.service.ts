import { Injectable } from '@angular/core';
import {HttpClient } from '@angular/common/http';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { Observable } from 'rxjs';
import { LoginRequestPayload } from '../login/login-request.payload';
import { LocalStorageService } from 'ngx-webstorage';
import { map, tap} from 'rxjs/operators';  
import { LoginResponse } from '../login/login-response.payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  url: String ="http://vps-f9968a88.vps.ovh.net:8080"; 

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService) { }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
   console.log('signup exection ...');
   this.clearStorage();
   
   return  this.httpClient.post(this.url+'/api/auth/signup', signupRequestPayload, {responseType: 'text' as 'json'});
  }

  login(loginRequestPayload: LoginRequestPayload){
    return this.httpClient.post<LoginResponse>(this.url+'/api/auth/login', loginRequestPayload)
    .pipe( 
      map(data => {
      if(data !== null){ 
      this.localStorage.store('authenticationToken', data.authenticationToken);
      this.localStorage.store('username', data.username);
      this.localStorage.store('refreshToken', data.refreshToken);
      this.localStorage.store('expiresAt', data.expiresAt);
      console.log("service true");
      return true;
       }else{
      return false;   
       }
    }))
  }



  refreshToken() {
    console.log('refresh token ....');
    const refreshTokenPayload = {
      refreshToken: this.getRefreshToken(),
      username: this.getUserName()
    }
    return this.httpClient.post<LoginResponse>(this.url+'/api/auth/refresh/token',
      refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.store('authenticationToken', response.authenticationToken);
        this.localStorage.store('expiresAt', response.expiresAt);
      }));
  }


  clearStorage(){
    this.localStorage.clear();
  }

  getJwtToken() {
    return this.localStorage.retrieve('authenticationToken');
  }

  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken');
  }

  getUserName() {
    return this.localStorage.retrieve('username');
  }

  getExpirationTime() {
    return this.localStorage.retrieve('expiresAt');
  }

}
