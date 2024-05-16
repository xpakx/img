import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ImagePageComponent } from './image/image-page/image-page.component';
import { ProfileComponent } from './gallery/profile/profile.component';

const routes: Routes = [
  { path: 'profile/:name', component: ProfileComponent },
  { path: 'profile/:name/image/:id', component: ImagePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
