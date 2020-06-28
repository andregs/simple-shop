import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddressComponent } from './address/address.component';
import { HasRoleGuard } from './auth/has-role.guard';
import { IsAuthenticatedGuard } from './auth/is-authenticated.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { UserListResolver } from './user/user-list.resolver';
import { UserComponent } from './user/user.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: '', canActivateChild: [IsAuthenticatedGuard], children: [
      { path: 'address', component: AddressComponent },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [HasRoleGuard],
        data: { role: 'ADMIN' },
      },
      {
        path: 'users',
        component: UserComponent,
        canActivate: [HasRoleGuard],
        data: { role: 'ADMIN' },
        resolve: { users: UserListResolver },
      },
    ],
  },
  // { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
