//
//  ZDSAppDelegate.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "ParserZSD.h"


@interface ZDSAppDelegate : NSObject <NSApplicationDelegate,NSUserNotificationCenterDelegate>{
    NSMutableArray *_allNotification;
    
    IBOutlet NSMenu *statusMenu;
    
    
    NSStatusItem *statusItem;
    NSTimer *timer;
    NSMutableArray *article;
}
@property (weak) IBOutlet NSMenuItem *menuArticle;
@property (assign) IBOutlet NSWindow *window;
@property (retain)NSMutableArray *article;

- (void)openUrl:(NSMenuItem *) item;
- (NSUserNotification *) showNotficationWithTitle:(NSString *)title AndWithInformation :(NSString *)infomation withImageAuteurs: (NSString *)url;
- (void) checkNew;
-(BOOL)findArrayInArray:(NSMutableArray *)arrayInArray withArray:(NSMutableArray *)arrayValue;
-(void)setArticle:(NSMutableArray *)a;
-(void)updateNotification:(NSNotification *) note;

@end
