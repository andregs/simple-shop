import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddressComponent } from './address/address.component';
import { IsAuthenticatedGuard } from './auth/is-authenticated.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { TableComponent } from './table/table.component';
import { HasRoleGuard } from './auth/has-role.guard';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: '', canActivateChild: [IsAuthenticatedGuard], children: [
      { path: 'dashboard', component: DashboardComponent, canActivate: [HasRoleGuard], data: {role: 'ADMIN'} },
      { path: 'table', component: TableComponent, canActivate: [HasRoleGuard], data: {role: 'ADMIN'} },
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
