import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../security/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  readonly loginForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
  ) {
    this.loginForm = this.fb.group({
      username: [null, Validators.required],
      password: [null, Validators.required],
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const credentials = {
      username: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value,
    };

    this.authService.login(credentials).subscribe(
      () => {
        console.info('login success');
        this.router.navigate(this.getReturnUrl());
      },

      // TODO display error message when bad credentials
      err => console.error('login error', err),
    );
  }

  private getReturnUrl(): string[] {
    const returnUrl = this.route.snapshot.queryParams.returnUrl;
    return [returnUrl || '/'];
  }

}
