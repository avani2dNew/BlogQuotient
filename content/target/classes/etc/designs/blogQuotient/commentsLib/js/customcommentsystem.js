(function($CQ, _, Backbone, SCF) {
    "use strict";
  
    var CustomComment = SCF.Components["social/commons/components/hbs/comments/comment"].Model;
    var CustomCommentView = SCF.Components["social/commons/components/hbs/comments/comment"].View;
 
    var CustomCommentSystem = SCF.Components["social/commons/components/hbs/comments"].Model;
    var CustomCommentSystemView = SCF.Components["social/commons/components/hbs/comments"].View;

    SCF.registerComponent('/apps/blogQuotient/components/global/comments/comment', CustomComment, CustomCommentView);
    SCF.registerComponent('/apps/blogQuotient/components/global/comments', CustomCommentSystem, CustomCommentSystemView);
 
})($CQ, _, Backbone, SCF);