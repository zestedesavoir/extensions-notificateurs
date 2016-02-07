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
@property (retain) NSString *nomArticle;
@property (retain) NSURL *urlArticle;
@property (retain) NSImage *imageArticle;

-(id)initWithNomArticle:(NSString*)nom Url:(NSURL *)url Image:(NSImage *)image;


@end
