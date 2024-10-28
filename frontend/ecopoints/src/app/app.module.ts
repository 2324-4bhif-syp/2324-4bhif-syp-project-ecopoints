import { RouterModule, Routes } from "@angular/router";
import { AppComponent } from "./app.component";
import { NgModule } from "@angular/core";
import { GraphOverviewComponent } from "./graph-overview/graph-overview.component";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule } from "@angular/common/http";
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 

const appRoutes: Routes = [
    { path: '', component: GraphOverviewComponent }
];

@NgModule({
    declarations: [
        AppComponent,
        GraphOverviewComponent
    ],
    imports: [

        BrowserModule,
        RouterModule.forRoot(appRoutes),
        HttpClientModule,
        FormsModule,
        BrowserAnimationsModule,
        
    ],
    bootstrap: [AppComponent]

})

export class AppModule { }
