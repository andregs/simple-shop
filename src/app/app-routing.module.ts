import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddressComponent } from './address/address.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AuthenticationGuard } from './security/authentication.guard';
import { TableComponent } from './table/table.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: '',   redirectTo: 'home', pathMatch: 'full' },
  {
    path: '', canActivateChild: [AuthenticationGuard], children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'table', component: TableComponent },
      { path: 'address', component: AddressComponent },
    ],
  },
  // { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
