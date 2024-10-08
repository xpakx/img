import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ImagePageComponent } from './image/image-page/image-page.component';
import { ProfileComponent } from './gallery/profile/profile.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { UploadComponent } from './image/upload/upload.component';
import { SettingsComponent } from './profile/settings/settings.component';
import { LikesComponent } from './feed/likes/likes.component';
import { FollowedComponent } from './feed/followed/followed.component';
import { EditComponent } from './image/edit/edit.component';
import { MessageComponent } from './message/message/message.component';
import { MessageFormComponent } from './message/message-form/message-form.component';
import { SearchComponent } from './search/search/search.component';

const routes: Routes = [
  { path: 'profile/:name', component: ProfileComponent },
  { path: 'profile/:name/image/:id', component: ImagePageComponent },
  { path: 'profile', component: SettingsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'upload', component: UploadComponent },
  { path: 'likes', component: LikesComponent },
  { path: '', component: FollowedComponent },
  { path: 'image/:id/edit', component: EditComponent },
  { path: 'priv', component: MessageComponent },
  { path: 'message', component: MessageFormComponent },
  { path: 'search', component: SearchComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
