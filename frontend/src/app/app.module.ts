import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GalleryComponent } from './gallery/gallery/gallery.component';
import { ImageComponent } from './image/image/image.component';
import { ModalComponent } from './image/modal/modal.component';
import { MiddleDirective } from './utils/middle.directive';
import { ImagePageComponent } from './image/image-page/image-page.component';
import { ProfileComponent } from './gallery/profile/profile.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { ErrorInterceptor } from './error/error.interceptor';
import { UploadComponent } from './image/upload/upload.component';
import { LikesComponent } from './feed/likes/likes.component';
import { FollowedComponent } from './feed/followed/followed.component';
import { EditComponent } from './image/edit/edit.component';
import { CommentComponent } from './comment/comment/comment.component';
import { MessageListComponent } from './message/message-list/message-list.component';
import { MessageComponent } from './message/message/message.component';
import { MessageFormComponent } from './message/message-form/message-form.component';
import { SearchComponent } from './search/search/search.component';
import { SearchPanelComponent } from './search/search-panel/search-panel.component';
import { PaginationComponent } from './utils/pagination/pagination.component';

@NgModule({ declarations: [
        AppComponent,
        GalleryComponent,
        ImageComponent,
        ModalComponent,
        MiddleDirective,
        ImagePageComponent,
        ProfileComponent,
        LoginComponent,
        RegisterComponent,
        UploadComponent,
        LikesComponent,
        FollowedComponent,
        EditComponent,
        CommentComponent,
        MessageListComponent,
        MessageComponent,
        MessageFormComponent,
        SearchComponent,
        SearchPanelComponent,
        PaginationComponent,
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule], providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorInterceptor,
            multi: true
        },
        provideHttpClient(withInterceptorsFromDi())
    ] })
export class AppModule { }
