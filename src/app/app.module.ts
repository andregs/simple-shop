import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AddressComponent } from './address/address.component';
import { AppComponent } from './app.component';
import { httpInterceptorProviders } from './app.interceptors';
import { AppRoutingModule } from './app.routing';
import { AuthModule } from './auth/auth.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { SharedModule } from './shared/shared.module';
import { TableComponent } from './table/table.component';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    TableComponent,
    AddressComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    SharedModule,
    AuthModule,
    AppRoutingModule,
  ],
  providers: [
    httpInterceptorProviders,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
