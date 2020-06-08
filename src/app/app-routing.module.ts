import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TableComponent } from './table/table.component';
import { AddressComponent } from './address/address.component';
import { LoginComponent } from './login/login.component';


const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'table', component: TableComponent },
  { path: 'address', component: AddressComponent },
  { path: 'login', component: LoginComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
