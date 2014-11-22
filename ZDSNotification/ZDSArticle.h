//
//  ZDSArticle.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 20/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ZDSArticle : NSObject{
    NSString *nomArticle;
    NSURL *urlArticle;
}
@property (readwrite, copy) NSString *nomArticle;
@property (readwrite, copy) NSURL *urlArticle;

-(id)initWithNomArticle:(NSString*)nom Url:(NSURL *)url;
-(void)openUrl;

@end
