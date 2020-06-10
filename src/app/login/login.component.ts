import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  readonly loginForm = this.fb.group({
    username: [null, Validators.required],
    password: [null, Validators.required],
  });

  private authenticated = false;

  constructor(private fb: FormBuilder, private service: LoginService) { }

  ngOnInit(): void {
    this.service.init();
  }

  ngOnDestroy(): void {
    if (!this.authenticated) {
      this.service.cancel();
    }
  }

  onSubmit() {
    const evt = { who: this.loginForm.controls.username.value };
    alert(`Hi, ${evt.who}!`);
    this.service.login(evt.who);
  }

}
