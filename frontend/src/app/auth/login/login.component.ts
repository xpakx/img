import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../auth.service';
import { AuthRequest } from '../dto/auth-request';
import { AuthResponse } from '../dto/auth-response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  error: boolean = false;
  errorMsg: String = "";

  constructor(private formBuilder: FormBuilder, private authService: AuthService) {
    this.loginForm = this.formBuilder.group({
      username: [''],
      password: [''],
    });
   }

  ngOnInit(): void {}

  login(): void {
    console.log(this.loginForm.value);
    if (this.loginForm.invalid) {
      return;
    }

    let request: AuthRequest = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
    };

    console.log(request);
    this.authService.login(request)
      .subscribe({
        next: (response: AuthResponse) => this.onLogin(response),
        error: (err: HttpErrorResponse) => this.onError(err)
      });
  }

  onLogin(response: AuthResponse) {
    this.error = false;
    localStorage.setItem('token', response.token.toString());
    localStorage.setItem('username', response.username.toString());
    localStorage.setItem('refresh', response.refresh_token.toString());
    // TODO: redir
  }

  onError(err: HttpErrorResponse) {
    console.log(err);
    this.error = true;
    this.errorMsg = err.message;
  }
}
