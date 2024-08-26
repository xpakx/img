import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ImagePageComponent } from './image/image-page/image-page.component';
import { ProfileComponent } from './gallery/profile/profile.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { UploadComponent } from './image/upload/upload.component';
import { SettingsComponent } from './profile/settings/settings.component';

const routes: Routes = [
  { path: 'profile/:name', component: ProfileComponent },
  { path: 'profile/:name/image/:id', component: ImagePageComponent },
  { path: 'profile', component: SettingsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'upload', component: UploadComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
