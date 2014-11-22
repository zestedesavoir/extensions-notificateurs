//
//  ZDSArticle.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 20/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ZDSArticle.h"

@implementation ZDSArticle
@synthesize nomArticle;
@synthesize urlArticle;
@synthesize imageArticle;
-(id)init{
    self = [super init];
    if (self){
        nomArticle = @"Aucun article";
        urlArticle = nil;
        imageArticle = nil;
        
    }
    return self;
}

-(id)initWithNomArticle:(NSString*)nom Url:(NSURL *)url Image:(NSImage *)image{
    self = [super init];
    if (self){
        nomArticle = nom;
        urlArticle = url;
        imageArticle = image;
    }
    return self;
}

@end
