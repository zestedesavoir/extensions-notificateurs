//
//  ZDSParserArticle.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 18/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//
#import "HTMLParser.h"
#import "ZDSParserArticle.h"
#import "ZDSArticle.h"
#import "ZDSAppDelegate.h"

@implementation ZDSParserArticle

-(NSMutableArray *) parseArticle{
    //Parse la page html : https://zestedesavoir.com/articles/
    NSMutableArray *articleArray= [[NSMutableArray alloc] init];
    NSString *nom =@"Article";
    NSURL *url = [NSURL URLWithString:@"https://zestedesavoir.com/articles/"];
    ZDSArticle *article = [[ZDSArticle alloc] initWithNomArticle:nom Url:url];
    
    [articleArray addObject:article];
    ZDSArticle *article1 = [[ZDSArticle alloc] initWithNomArticle:@"www.google.com" Url:nil];
    [articleArray addObject:article1];
    [article1 nomArticle];
    NSLog(@"%@",[[articleArray objectAtIndex:1] nomArticle]);
    return articleArray;
}

@end
